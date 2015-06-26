package org.strandz.data.wombatrescue.objects.cayenne;

import org.strandz.data.wombatrescue.objects.cayenne.auto._WombatrescueDataMap;

public class WombatrescueDataMap extends _WombatrescueDataMap {

    private static WombatrescueDataMap instance;

    private WombatrescueDataMap() {}

    public static WombatrescueDataMap getInstance() {
        if(instance == null) {
            instance = new WombatrescueDataMap();
        }

        return instance;
    }
}
