package org.strandz.data.wombatrescue.objects.cayenne.client;

import org.strandz.data.wombatrescue.objects.cayenne.client.auto._WombatrescueDataMap;

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
