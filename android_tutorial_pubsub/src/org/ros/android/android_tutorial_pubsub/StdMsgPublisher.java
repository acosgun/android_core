package org.ros.android.android_tutorial_pubsub;

import org.ros.node.AbstractNodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;


/**
 * Created by acosgun on 4/2/15.
 */
public class StdMsgPublisher extends AbstractNodeMain {

    Publisher<std_msgs.String> publisher;
    String node_name;
    String topic_name;

    public StdMsgPublisher(String node_name, String topic_name)
    {
    this.node_name = node_name;
    this.topic_name = topic_name;
    }


    @Override
    public GraphName getDefaultNodeName()
    {
        //return GraphName.of("rosjava_tutorial_pubsub/std_msg_publisher");
        return GraphName.of(node_name);
    }

    @Override
    public void onStart(final ConnectedNode connectedNode)
    {
        publisher = connectedNode.newPublisher(topic_name, std_msgs.String._TYPE);
    }

    public void publish(String msg)
    {
        std_msgs.String str = publisher.newMessage();
        str.setData(msg);
        publisher.publish(str);
    }
}
