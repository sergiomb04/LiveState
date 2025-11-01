package me.imsergioh.livecore.instance.connection;

import java.util.Map;

public interface IConnectionAction {

    String getName();

    void onAction(LiveStateClient client, Map<String, Object> objectMap);

}
