package org.hinoob.setup;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class SetupDataManager {

    private Map<Player, SetupData> map = new HashMap<>();

    public void addData(Player player){
        map.put(player, new SetupData());
    }

    public SetupData getData(Player player){
        return map.getOrDefault(player, null);
    }

}
