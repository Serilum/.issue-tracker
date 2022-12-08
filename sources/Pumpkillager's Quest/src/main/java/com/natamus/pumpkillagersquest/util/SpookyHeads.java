/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.3, mod version: 2.3.
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

package com.natamus.pumpkillagersquest.util;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.HeadFunctions;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpookyHeads {
    public static Map<String, Pair<String, String>> pumpkinHeadData = new HashMap<String, Pair<String, String>>();
    public static Map<String, Pair<String, String>> spookyHeadData = new HashMap<String, Pair<String, String>>();
    public static Map<String, Pair<String, String>> allHeadData = new HashMap<String, Pair<String, String>>();

    public static void initPumpkinHeadData() {
        pumpkinHeadData.put("Black Jack o'Lantern", new Pair<String, String>("b9848ccd-f7ba-4746-811c-18812aa66f0c", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M4NjZlNDIzOTU1MTQyOGY5YzgxNGQ5ZGMxMWU3MDAyZTA0M2I4N2Y1OTBmMWZkY2I0ZmY2YmI0OTdiOGUxOCJ9fX0="));
        pumpkinHeadData.put("Blue Jack o'Lantern", new Pair<String, String>("8bf0a045-b8f8-4e9b-8853-d1f77e072230", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjM4NzI2NWQxMzE3Mjg0Nzk1MGVhNTA2YmEwZmYyYjA0Yjc5OTk4Mjg1MjVhMDMyOTA1N2E5ODk4NWRiMjVmYyJ9fX0="));
        pumpkinHeadData.put("Brown Jack o'Lantern", new Pair<String, String>("ad8086e3-dc26-49d8-9138-3ebdae909860", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjI3MDQyOTM0YjJhNGIwNDZmODdlZDA5MDVmZDA5MmE4OTNhZmZjNWRiZWUwOTU1NjQzNjliY2M2YmVhYWJiIn19fQ=="));
        pumpkinHeadData.put("Cyan Jack o'Lantern", new Pair<String, String>("16a19ecc-534a-4386-970a-6cf5b0a51ef8", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTIzYjBjN2YyMTNmZDUxZGQ4ODhlYTJjZmNjZGQ3YTI4YjI0N2E5ZDQ4ZmM1NDk3YjkzNDNmYmNhZGE3NjMyMyJ9fX0="));
        pumpkinHeadData.put("Gray Jack o'Lantern", new Pair<String, String>("ec174968-5c42-41e0-9402-ec76c82c3ed9", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFiM2QxMmQxNmM5MDZlMjllODI1ZjU0M2JiYjQ2OGE2MTJjMzgzYjUzM2Q0Zjc4MjY4MDYzNjBiN2FlYzNiYiJ9fX0="));
        pumpkinHeadData.put("Green Jack o'Lantern", new Pair<String, String>("918b779e-2076-4eaa-b559-bea468fcec95", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ3YzMyMzY3NmVkMWQyZTUyNjIyYTA2ZjJmNmJhNTE4MTJlNmM4NmE2NjNhZjUyYjBiNDY1NDlhNWU1ZmI1MSJ9fX0="));
        pumpkinHeadData.put("Light Blue Jack o'Lantern", new Pair<String, String>("7418062a-7444-471b-9fc1-3b72e67ef8d1", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhjZTRkMjY4OTU2YmY0NzA4NjNiMGM4NTk1ZGM1ZjA2OTRkNjA0YmNiYzg1OWU2ZTY2MThmYTMwMTJkMGUwYyJ9fX0="));
        pumpkinHeadData.put("Lime Jack o'Lantern", new Pair<String, String>("98dbe53a-9629-46b5-8d12-66f0e7b72930", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzY3MDk5NTQ1MGJkYjcxMDlmYTViZmU2ZDEzZmVlZGUzMjRmNDM5NWZjOTAwOTAwMGM4MGVlNTU0ZmFmYTYzNyJ9fX0="));
        pumpkinHeadData.put("Magenta Jack o'Lantern", new Pair<String, String>("41266191-9435-460f-ab94-238a9a3c420d", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ5ZTA3N2ZmYWFlNWJiYjk1Mjk5MzAyZDY0ZDkzMjNkNDYyYzRkZjFmMjhjYTI0NjZlOTFmYWE4NmM0M2MxNSJ9fX0="));
        pumpkinHeadData.put("Neon Green Jack o'Lantern", new Pair<String, String>("a30e62b8-6d2f-488c-b771-92baeb4c6ca8", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmFmYmFkMDVjMWQ2YWVlY2I1YmFkMGMwNDI1N2E0YTFmZDM1MzFiZWY0NTY3OWJmNGRjYzM5ZjE2MGUyZTg2OSJ9fX0="));
        pumpkinHeadData.put("Orange Jack o'Lantern", new Pair<String, String>("fab797d2-c0e5-4683-9ec8-38f7e71ec354", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMyMWNlNDcwZjIzMmI5ZTczZmNlMDkxY2U4NDhkZTkzODYwYTAzMzgxZDMxMDBjNDJiMzk2YzAyNTRiZTBlZiJ9fX0="));
        pumpkinHeadData.put("Pink Jack o'Lantern", new Pair<String, String>("d1971c42-e71a-434f-aaff-c0fb8e1dc15c", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTUyZjI4YWMyMmUwOWM1Yzc1NWJlNWVmOWI4N2NiZmI3ZjI4N2ZkZDg3Y2Y5Y2E1OGNlYTMyM2RiZmYyY2VjIn19fQ=="));
        pumpkinHeadData.put("Purple Jack o'Lantern", new Pair<String, String>("e701d522-436c-4d72-ab12-336ebcb71d8b", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQzYTFjMTU1ZmRlZWNlNmVmNzA0ZWQ1NzJkODU0ZGJkNjIxYzRmMDc5OTkwYzViYWY5MzE5ZWMyZTA2ZTI2YiJ9fX0="));
        pumpkinHeadData.put("Red Jack o'Lantern", new Pair<String, String>("e0b491e6-3b20-49db-a214-2673c3e57c0c", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI3NzE5OGI2YzcwMDM2ZmU0YWMzNTk2MWFlMDcxZGMzY2RkMDE3NmVhMzdmMDkxMzU4MmEwMTVlMGIzNzNjMCJ9fX0="));
        pumpkinHeadData.put("White Jack o'Lantern", new Pair<String, String>("5facef5e-24ef-43f5-905e-8467922053dd", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVkYTNhMWRjNGYyMzU5YzliMWU0M2Q5MjUzMTk2ZTZhYjVhMmQyNDg0N2EwMzg1N2NkN2I4M2Y2ODU0YWU1ZCJ9fX0="));
        pumpkinHeadData.put("Yellow Jack o'Lantern", new Pair<String, String>("70ad54c6-2b3d-4cf3-abb1-d9190abd895d", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRhOTg5ZmFiNTUzZjM3OTE5MjYzOGY3MjU4OTMxM2Q5MTNmYzBiMzVlMGNjZjdiYWQwNzFkODcyNjA3ZWUzMSJ9fX0="));

        spookyHeadData.put("Pumpkin", new Pair<String, String>("f441b2e7-7b8a-42f8-8a32-3fe2a4e2d3d4", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0ZjVhZGNmMDQ4MDA1NWY5NjllMjBhMjg0ZDc1NDRlM2UxOWVmMzQwYTAzN2M2OGQ2ZTY1OWI3ZmE0OTEyOSJ9fX0="));
        spookyHeadData.put("Carved Pumpkin", new Pair<String, String>("4bf6db5f-f623-4e1f-88f6-aaac74588c12", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNhZDc5MjA3YzNmOGJiZjRiMWQ1MzJjZmU4NmRjY2I1N2QyYzk3YWVlYjUxZWYwMGE2NjBhZmRjZWFiNWZhOSJ9fX0="));
        spookyHeadData.put("Jack o'Lantern", new Pair<String, String>("775d50d9-f87d-42be-b3eb-f435beb60ff9", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFkNWQxMmE4YjUyMGNiNTdiZDlkMTAwNTkwMjBkYTEzOTQ4NTMyNDcyMGUwZDUxODgwZWU4NDE5NWRmZGNlMiJ9fX0="));
        spookyHeadData.put("Pumpkillager's Head", new Pair<String, String>("b89788b1-f84f-4e54-854d-cc84ede0d3d3", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I2ODY1ZmE3YTJhMTc3NTk3NzQzYmUyMzAzZDY4YmRlZTYzNjBhZWEyOTQ1YzQ3MWQ4MjU1Y2JlNDYifX19"));
        spookyHeadData.put("Ghost Knight Head", new Pair<String, String>("69198f1e-3f26-4866-b786-836e6b0957d9", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJhYTQxZTI2MDI2MmU1ZjRjOTFlZmVmNzNiYjJmNWI5Y2Y3NTY0OTE1MDE3NDkwNzQ2ZGM4Y2JhYWZjODE1YiJ9fX0="));
        spookyHeadData.put("Ghost Rider Head", new Pair<String, String>("573f4ccd-f570-4adc-b179-b9d24a76f6be", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBmZGRlZDcxN2Q3OWE3ZjljYTM3ZmQxODdkMGViNDM4ZTIyNGQzMjY5ZGUzOTJlZDRlOTgyYWNjMTlkMWQzIn19fQ=="));
        spookyHeadData.put("Golden Horse Head", new Pair<String, String>("4ae3e5dd-8b53-40ec-b580-3235cc633414", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q4ODY0M2IwNDg2Nzk2MjRhNDJlZTA0NjY2YTQ4NjVlYzE2ODcxYzczMjc5NzM0NjVhZjZiZDVhYmRkOGNhNyJ9fX0="));

        allHeadData.putAll(pumpkinHeadData);
        allHeadData.putAll(spookyHeadData);
    }

    public static List<ItemStack> getAllPumpkinHeads() {
        List<ItemStack> pumpkinHeads = new ArrayList<ItemStack>();

        for (String pumpkinHeadName : pumpkinHeadData.keySet()) {
            Pair<String, String> pair = pumpkinHeadData.get(pumpkinHeadName);
            pumpkinHeads.add(HeadFunctions.getTexturedHead(pumpkinHeadName, pair.getSecond(), pair.getFirst(), 1));
        }

        return pumpkinHeads;
    }

    public static ItemStack getSpookyOrPumpkinHead(String headName, int amount) {
        Pair<String, String> pair = allHeadData.get(headName);
        return HeadFunctions.getTexturedHead(headName, pair.getSecond(), pair.getFirst(), amount);
    }

    public static ItemStack getPumpkin(Integer amount) {
        return getSpookyOrPumpkinHead("Pumpkin", amount);
    }
    public static ItemStack getCarvedPumpkin(Integer amount) {
        return getSpookyOrPumpkinHead("Carved Pumpkin", amount);
    }
    public static ItemStack getJackoLantern(Integer amount) {
        return getSpookyOrPumpkinHead("Jack o'Lantern", amount);
    }
    public static ItemStack getEvilJackoLantern(Integer amount) {
        return getSpookyOrPumpkinHead("Pumpkillager's Head", amount);
    }
    public static ItemStack getGhostKnightHead(Integer amount) {
        return getSpookyOrPumpkinHead("Ghost Knight Head", amount);
    }
    public static ItemStack getGhostRiderHead(Integer amount) {
        return getSpookyOrPumpkinHead("Ghost Rider Head", amount);
    }
    public static ItemStack getGoldHorseHead(Integer amount) {
        return getSpookyOrPumpkinHead("Golden Horse Head", amount);
    }
}
