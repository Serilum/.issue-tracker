/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.45.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.schematic;

import com.mojang.brigadier.StringReader;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Schematic {
	private int size;
	private short width;
	private short height;
	private short length;
	private int offsetX;
	private int offsetY;
	private int offsetZ;
	private boolean oldVersion;
	private HashMap<Integer, String> palette;
	private SchematicBlockObject[] blockObjects;
	private List<CompoundTag> blockEntities;
	private List<Pair<BlockPos, CompoundTag>> entities;

	public Schematic(InputStream inputStream) {
		String type = "";
		try {
			CompoundTag nbtdata = NbtIo.readCompressed(inputStream);
			inputStream.close();

			if (nbtdata.contains("Length")) {
				length = nbtdata.getShort("Length");
				width = nbtdata.getShort("Width");
				height = nbtdata.getShort("Height");
			}
			else {
				ListTag sizeList = nbtdata.getList("size", Tag.TAG_INT);
				length = (short)sizeList.getInt(0);
				width = (short)sizeList.getInt(1);
				height = (short)sizeList.getInt(2);
			}

			size = length * width * height;

			if (nbtdata.contains("entities")) {
				type = "nbt";
			}
			else if (nbtdata.contains("DataVersion")) {
				type = "schem";
			}
			else {
				type = "schematic";
			}

			this.blockObjects = new SchematicBlockObject[size];
			this.entities = new ArrayList<Pair<BlockPos, CompoundTag>>();

			if (type.equals("schem")) {
				byte[] blocks = nbtdata.getByteArray("BlockData");

				CompoundTag palette = nbtdata.getCompound("Palette");
				this.palette = new HashMap<Integer, String>();
				for (String k : palette.getAllKeys()) {
					this.palette.put(palette.getInt(k), k);
				}

				int counter = 0;
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < length; j++) {
						for (int k = 0; k < width; k++) {
							BlockPos pos = new BlockPos(k, i , j);
							int id = blocks[counter];

							if (id < 0) {
								id *= -1;
							}

							BlockState state = getStateFromID(id);

							blockObjects[counter] = new SchematicBlockObject(pos, state);
							counter++;
						}
					}
				}

				ListTag tileentitynbtlist = nbtdata.getList("BlockEntities", Tag.TAG_COMPOUND);
				this.blockEntities = new ArrayList<CompoundTag>();

				for (int i = 0; i < tileentitynbtlist.size(); i++) {
					this.blockEntities.add(tileentitynbtlist.getCompound(i));
				}

				CompoundTag offsetCompoundTag = nbtdata.getCompound("Metadata");
				offsetX = offsetCompoundTag.getInt("WEOffsetX");
				offsetY = offsetCompoundTag.getInt("WEOffsetY");
				offsetZ = offsetCompoundTag.getInt("WEOffsetZ");

				return;
			}
			else if (type.equals("schematic")) {
				byte[] blockIDs_byte = nbtdata.getByteArray("Blocks");
				int[] blockIDs = new int[size];
				for (int x = 0; x < blockIDs_byte.length; x++) {
					blockIDs[x] = Byte.toUnsignedInt(blockIDs_byte[x]);
				}

				byte[] metadata = nbtdata.getByteArray("Data");

				int counter = 0;
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < length; j++) {
						for (int k = 0; k < width; k++) {
							BlockPos pos = new BlockPos(k, i , j);
							BlockState state = getStateFromOldIds(blockIDs[counter], metadata[counter]);
							blockObjects[counter] = new SchematicBlockObject(pos, state);
							counter++;
						}
					}
				}

				ListTag tileentitynbtlist = nbtdata.getList("TileEntities", Tag.TAG_COMPOUND);
				this.blockEntities = new ArrayList<CompoundTag>();

				for (int i = 0; i < tileentitynbtlist.size(); i++) {
					CompoundTag compound = tileentitynbtlist.getCompound(i);
					int i0 = compound.getInt("x");
					int i1 = compound.getInt("y");
					int i2 = compound.getInt("z");
					compound.putIntArray("Pos", new int[] {i0, i1, i2});
					this.blockEntities.add(compound);
				}

				offsetX = nbtdata.getInt("WEOffsetX");
				offsetY = nbtdata.getInt("WEOffsetY");
				offsetZ = nbtdata.getInt("WEOffsetZ");

				return;
			}
			else if (type.equals("nbt")) {
				ListTag paletteNBTList = nbtdata.getList("palette", Tag.TAG_COMPOUND);

				this.palette = new HashMap<Integer, String>();
				for (int i = 0; i < paletteNBTList.size(); i++) {
					CompoundTag compound = paletteNBTList.getCompound(i);
					String value = compound.getString("Name");

					if (compound.contains("Properties")) {
						StringBuilder metaData = new StringBuilder("[");

						CompoundTag propertyCompound = compound.getCompound("Properties");
						for (String propertyKey : propertyCompound.getAllKeys()) {
							if (!metaData.toString().equals("[")) {
								metaData.append(",");
							}

							metaData.append(propertyKey).append("=").append(propertyCompound.get(propertyKey));
						}

						metaData.append("]");
						value += metaData;
					}

					this.palette.put(i, value);
				}

				this.blockEntities = new ArrayList<CompoundTag>();

				ListTag blocksNBTList = nbtdata.getList("blocks", Tag.TAG_COMPOUND);
				for (int i = 0; i < blocksNBTList.size(); i++) {
					CompoundTag compound = blocksNBTList.getCompound(i);

					ListTag posList = compound.getList("pos", Tag.TAG_INT);
					int i0 = posList.getInt(0);
					int i1 = posList.getInt(1);
					int i2 = posList.getInt(2);

					BlockPos pos = new BlockPos(i0, i1, i2);

					BlockState state = getStateFromID(compound.getInt("state"));
					blockObjects[i] = new SchematicBlockObject(pos, state);

					if (compound.contains("nbt")) {
						CompoundTag blockEntityCompound = compound.getCompound("nbt");
						blockEntityCompound.putIntArray("Pos", new int[] {i0, i1, i2});
						blockEntityCompound.putString("Id", blockEntityCompound.getString("id"));
						blockEntityCompound.remove("id");
						this.blockEntities.add(blockEntityCompound);
					}
				}

				ListTag entitiesNBTList = nbtdata.getList("entities", Tag.TAG_COMPOUND);
				for (int i = 0; i < entitiesNBTList.size(); i++) {
					CompoundTag compound = entitiesNBTList.getCompound(i);

					CompoundTag entityCompound = compound.getCompound("nbt");

					ListTag posList = compound.getList("blockPos", Tag.TAG_INT);
					int i0 = posList.getInt(0);
					int i1 = posList.getInt(1);
					int i2 = posList.getInt(2);

					this.entities.add(new Pair<BlockPos, CompoundTag>(new BlockPos(i0, i1, i2), entityCompound));
				}

				offsetX = 0;
				offsetY = 0;
				offsetZ = 0;

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.err.println("Can't load " + type + " Schematic file.");
		this.width = 0;
		this.height = 0;
		this.length = 0;
		this.offsetX = 0;
		this.offsetY = 0;
		this.offsetZ = 0;
		this.size = 0;
		this.blockObjects = null;
		this.palette = null;
		this.blockEntities = null;
	}

	public boolean isOldVersion() {
		return oldVersion;
	}

	private BlockState getStateFromOldIds(int blockID, byte meta) {
		return Block.stateById(blockID);
	}

	public BlockState getBlockState(BlockPos pos) {
		for (SchematicBlockObject obj : this.blockObjects) {
			if (obj.getPosition().equals(pos)) {
				return obj.getState();
			}
		}
		return Blocks.AIR.defaultBlockState();
	}

	public int getSize() {
		return size;
	}

	public SchematicBlockObject[] getBlocks() {
		return blockObjects;
	}

	public BlockState getStateFromID(int id) {
		String iblockstateS = this.palette.get(id);

		try {
			return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), new StringReader(iblockstateS), false).blockState();
		} catch (Exception ex) {
			return Blocks.AIR.defaultBlockState();
		}
	}

	public List<CompoundTag> getBlockEntities() {
		return this.blockEntities;
	}

	public List<Pair<BlockPos, CompoundTag>> getEntityRelativePosPairs() {
		return this.entities;
	}

	public CompoundTag getTileEntity(BlockPos pos) {
		for (CompoundTag compound : this.blockEntities) {
			int[] pos1 = compound.getIntArray("Pos");

			if (pos1[0] == pos.getX() && pos1[1] == pos.getY() && pos1[2] == pos.getZ()) {
				return compound;
			}
		}
		return null;
	}

	public BlockPos getBlockPosFromCompoundTag(CompoundTag compoundTag) {
		int[] pos = compoundTag.getIntArray("Pos");
		return new BlockPos(pos[0], pos[1], pos[2]);
	}

	public short getWidth() {
		return width;
	}
	public short getHeight() {
		return height;
	}
	public short getLength() {
		return length;
	}

	public int getOffsetX() {
		return offsetX;
	}
	public int getOffsetY() {
		return offsetY;
	}
	public int getOffsetZ() {
		return offsetZ;
	}
}