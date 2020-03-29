package com.tisanehealth.Fragment.Team;


import android.util.Log;
import android.view.MenuItem;

import com.tisanehealth.Model.tree.TreeL4P7;
import com.tisanehealth.Model.tree.TreeResponse;

import de.blox.graphview.Graph;
import de.blox.graphview.GraphAdapter;
import de.blox.graphview.Node;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;

public class BuchheimWalkerActivity extends GraphActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final BuchheimWalkerConfiguration.Builder builder = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300);
        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(builder.build()));
        adapter.notifyInvalidated();
        return true;
    }

    @Override
    public Graph createGraph(TreeResponse treeResponse) {
        Graph graph = new Graph();
        Node node1;
        Node node2;
        Node node3;
        Node node4;
        Node node5;
        Node node6;
        Node node8;
        Node node7;
        Node node9;
        Node node10;
        Node node11;
        Node node12;
        Node node13;
        Node node14;
        Node node15;
        try {
            node1 = new Node(treeResponse.getTreeL1P1().get(0));
            node2 = new Node(treeResponse.getTreeL2P1().get(0));
            node3 = new Node(treeResponse.getTreeL2P2().get(0));
            node4 = new Node(treeResponse.getTreeL3P1().get(0));
            node5 = new Node(treeResponse.getTreeL3P2().get(0));
            node6 = new Node(treeResponse.getTreeL3P3().get(0));
            node7 = new Node(treeResponse.getTreeL3P4().get(0));
            node8 = new Node(treeResponse.getTreeL4P1().get(0));
            node9 = new Node(treeResponse.getTreeL4P2().get(0));
            node10 = new Node(treeResponse.getTreeL4P3().get(0));
            node11 = new Node(treeResponse.getTreeL4P4().get(0));
            node12 = new Node(treeResponse.getTreeL4P5().get(0));
            node13 = new Node(treeResponse.getTreeL4P6().get(0));
            node14 = new Node(treeResponse.getTreeL4P7().get(0));
            node15 = new Node(treeResponse.getTreeL4P8().get(0));



            graph.addEdge(node1, node2);
            graph.addEdge(node1, node3);
            graph.addEdge(node2, node4);
            graph.addEdge(node2, node5);
            graph.addEdge(node3, node6);
            graph.addEdge(node3, node7);
            graph.addEdge(node4, node8);
            graph.addEdge(node4, node9);
            graph.addEdge(node5, node10);
            graph.addEdge(node5, node11);
            graph.addEdge(node6, node12);
            graph.addEdge(node6, node13);
            graph.addEdge(node7, node14);
            graph.addEdge(node7, node15);

        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            try {
                graph.addEdge(node1, node2);
                graph.addEdge(node1, node3);
                graph.addEdge(node2, node4);
                graph.addEdge(node2, node5);
                graph.addEdge(node3, node6);
                graph.addEdge(node3, node7);
                graph.addEdge(node4, node8);
                graph.addEdge(node4, node9);
                graph.addEdge(node5, node10);
                graph.addEdge(node5, node11);
                graph.addEdge(node6, node12);
                graph.addEdge(node6, node13);
                graph.addEdge(node7, node14);
                graph.addEdge(node7, node15);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


        return graph;
    }

    @Override
    public void setAlgorithm(GraphAdapter adapter) {
        final BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build();
        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));
    }
}
