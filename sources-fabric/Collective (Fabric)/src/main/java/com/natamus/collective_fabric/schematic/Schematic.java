/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.11.
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
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Schematic {

	private int size;
	private short width;
	private short height;
	private short length;
	private boolean oldVersion;
	private HashMap<Integer, String> palette;
	private SchematicBlockObject[] blockObjects;
	private List<CompoundTag> blockEntities;

	public Schematic(InputStream inputStream) {
		try {
			CompoundTag nbtdata = NbtIo.readCompressed(inputStream);
			inputStream.close();

			width = nbtdata.getShort("Width");
			height = nbtdata.getShort("Height");
			length = nbtdata.getShort("Length");
			size = width * height * length;
			this.oldVersion = !nbtdata.contains("DataVersion");

			blockObjects = new SchematicBlockObject[size];

			if (!oldVersion) {
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

							if (id < 0) id *= -1;

							BlockState state = getStateFromID(id);

							blockObjects[counter] = new SchematicBlockObject(pos, state);

							counter++;

						}
					}
				}

				ListTag tileentitynbtlist = nbtdata.getList("BlockEntities", 10);
				this.blockEntities = new ArrayList<CompoundTag>();

				for (int i = 0; i < tileentitynbtlist.size(); i++) {
					this.blockEntities.add(tileentitynbtlist.getCompound(i));
				}

			} else {

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

				ListTag tileentitynbtlist = nbtdata.getList("TileEntities", 10);
				this.blockEntities = new ArrayList<CompoundTag>();

				for (int i = 0; i < tileentitynbtlist.size(); i++) {

					CompoundTag compound = tileentitynbtlist.getCompound(i);
					int i1 = compound.getInt("x");
					int i2 = compound.getInt("y");
					int i3 = compound.getInt("z");
					compound.putIntArray("Pos", new int[] {i1, i2, i3});
					this.blockEntities.add(compound);

				}
			}
		} catch (Exception e) {
			System.err.println("ERROR Cant load Schematic");
			e.printStackTrace();
			this.width = 0;
			this.height = 0;
			this.length = 0;
			this.size = 0;
			this.blockObjects = null;
			this.palette = null;
			this.blockEntities = null;
		}
	}
	
	public boolean isOldVersion() {
		return oldVersion;
	}
	
	private BlockState getStateFromOldIds(int blockID, byte meta) {
		return Block.stateById(blockID);
	}
	
	public BlockState getBlockState(BlockPos pos) {
		
		for (SchematicBlockObject obj : this.blockObjects) {
			if (obj.getPosition().equals(pos)) return obj.getState();
		}
		return Blocks.AIR.defaultBlockState();
		
	}
	
	public int getSize() {
		return size;
	}
	
	public SchematicBlockObject[] getBlocks() {
		return blockObjects;
	}

	@SuppressWarnings( "deprecation" )
	public BlockState getStateFromID(int id) {
		String iblockstateS = this.palette.get(id);
		
		try {
			return BlockStateParser.parseForBlock(Registry.BLOCK, new StringReader(iblockstateS), false).blockState();
		} catch (Exception ex) {
			return Blocks.AIR.defaultBlockState();
		}
	}
	
	public List<CompoundTag> getBlockEntities() {
		return this.blockEntities;
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
	
}
