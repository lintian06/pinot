package com.linkedin.pinot.broker.broker.helix;

import org.apache.helix.HelixManager;
import org.apache.helix.NotificationContext;
import org.apache.helix.model.Message;
import org.apache.helix.participant.statemachine.StateModel;
import org.apache.helix.participant.statemachine.StateModelFactory;
import org.apache.helix.participant.statemachine.StateModelInfo;
import org.apache.helix.participant.statemachine.Transition;
import org.apache.log4j.Logger;

import com.linkedin.pinot.controller.helix.core.HelixHelper;
import com.linkedin.pinot.routing.HelixExternalViewBasedRouting;


/**
 * Broker Resource layer state model to take over how to operate on:
 * Adding an external view to routing table.
 * 
 * @author xiafu
 *
 */
public class BrokerResourceOnlineOfflineStateModelFactory extends StateModelFactory<StateModel> {

  private static HelixManager _helixManager;
  private static HelixExternalViewBasedRouting _helixExternalViewBasedRouting;

  public BrokerResourceOnlineOfflineStateModelFactory(HelixManager helixManager,
      HelixExternalViewBasedRouting helixExternalViewBasedRouting) {
    _helixManager = helixManager;
    _helixExternalViewBasedRouting = helixExternalViewBasedRouting;
  }

  public static String getStateModelDef() {
    return "BrokerResourceOnlineOfflineStateModel";
  }

  @Override
  public StateModel createNewStateModel(String resourceName) {
    BrokerResourceOnlineOfflineStateModel brokerResourceOnlineOfflineStateModel =
        new BrokerResourceOnlineOfflineStateModel();
    return brokerResourceOnlineOfflineStateModel;
  }

  @StateModelInfo(states = "{'OFFLINE','ONLINE', 'DROPPED'}", initialState = "OFFLINE")
  public static class BrokerResourceOnlineOfflineStateModel extends StateModel {
    private static final Logger LOGGER = Logger.getLogger(BrokerResourceOnlineOfflineStateModelFactory.class);

    @Transition(from = "OFFLINE", to = "ONLINE")
    public void onBecomeOnlineFromOffline(Message message, NotificationContext context) {
      try {
        System.out.println("BrokerResourceOnlineOfflineStateModel.onBecomeOnlineFromOffline() : " + message);
        LOGGER.info("BrokerResourceOnlineOfflineStateModel.onBecomeOnlineFromOffline() : " + message);
        String resourceName = message.getPartitionName();
        _helixExternalViewBasedRouting.markDataResourceOnline(
            resourceName,
            HelixHelper.getExternalViewForResouce(_helixManager.getClusterManagmentTool(),
                _helixManager.getClusterName(), resourceName));
      } catch (Exception e) {
        LOGGER.error(e);
      }
    }

    @Transition(from = "ONLINE", to = "OFFLINE")
    public void onBecomeOfflineFromOnline(Message message, NotificationContext context) {
      try {
        System.out.println("BrokerResourceOnlineOfflineStateModel.onBecomeOfflineFromOnline() : " + message);
        LOGGER.info("BrokerResourceOnlineOfflineStateModel.onBecomeOfflineFromOnline() : " + message);
        String resourceName = message.getResourceName();
        _helixExternalViewBasedRouting.markDataResourceOffline(resourceName);
      } catch (Exception e) {
        LOGGER.error(e);
      }
    }

    @Transition(from = "OFFLINE", to = "DROPPED")
    public void onBecomeDroppedFromOffline(Message message, NotificationContext context) {
      try {
        System.out.println("BrokerResourceOnlineOfflineStateModel.onBecomeDroppedFromOffline() : " + message);
        LOGGER.info("BrokerResourceOnlineOfflineStateModel.onBecomeDroppedFromOffline() : " + message);
        String resourceName = message.getResourceName();
        _helixExternalViewBasedRouting.markDataResourceOffline(resourceName);
      } catch (Exception e) {
        LOGGER.error(e);
      }
    }

    @Transition(from = "ONLINE", to = "DROPPED")
    public void onBecomeDroppedFromOnline(Message message, NotificationContext context) {
      try {
        System.out.println("BrokerResourceOnlineOfflineStateModel.onBecomeDroppedFromOnline() : " + message);
        LOGGER.info("BrokerResourceOnlineOfflineStateModel.onBecomeDroppedFromOnline() : " + message);
        String resourceName = message.getResourceName();
        _helixExternalViewBasedRouting.markDataResourceOffline(resourceName);
      } catch (Exception e) {
        LOGGER.error(e);
      }
    }
  }
}