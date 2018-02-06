package com.tuhailong.graph;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 有向图的邻接链表表示
 * @author tuhailong
 */
public class DirectedGraph<T> {

    // 顶点
    private class VertexNode<E> {
        // 顶点信息
        E info;
        // 指向第一条依附该顶点的边
        AdjacentNode firstEdge;
    }

    // 顶点对应的邻接表的结点
    private class AdjacentNode {
        // 该边的对端顶点在顶点数组中的位置
        int vexIdx;
        // 指向下一条边的指针
        AdjacentNode nextEdge;
    }

    // 顶点数组
    private VertexNode<T>[] mVertexes;

    @SuppressWarnings("unchecked")
    public DirectedGraph(T[] vexs, T[][] edges) {
        // 顶点数量
        int vLen = vexs.length;
        // 边的数量
        int eLen = edges.length;

        // 顶点赋初值
        mVertexes = (VertexNode<T>[])new VertexNode[vLen];
        for (int i = 0; i < vLen; i++) {
            mVertexes[i] = new VertexNode<T>();
            mVertexes[i].info = vexs[i];
            mVertexes[i].firstEdge = null;
        }

        for (int i = 0; i < eLen; i++) {
            // 边edges[i]的起始顶点在mVertexes[i]的位置
            int sp = index(edges[i][0]);
            // 边edges[i]的终止顶点在mVertexes[i]的位置
            int ep = index(edges[i][1]);
            if (sp == -1 || ep == -1) {
                continue;
            }
            // 初始化上sNode
            AdjacentNode sNode = new AdjacentNode();
            sNode.vexIdx = ep;
            // 将sNode链接到"顶点sp所指向的链表的末尾"
            if (mVertexes[sp].firstEdge == null) {
                mVertexes[sp].firstEdge = sNode;
            } else {
                linkLast(mVertexes[sp].firstEdge, sNode);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private DirectedGraph(VertexNode<T>[] vertexes) {
        int vLen = vertexes.length;
        mVertexes = (VertexNode<T>[])new VertexNode[vLen];
        for (int i = 0; i< vLen; i++) {
            mVertexes[i] = vertexes[i];
        }
    }

    /**
     * 将node节点链接到list的尾部
     */
    private void linkLast(AdjacentNode list, AdjacentNode node) {
        AdjacentNode p = list;
        while (p.nextEdge != null) {
            p = p.nextEdge;
        }
        p.nextEdge = node;
    }

    /**
     * 返回item在mVertexes中的位置
     */
    public int index(T item) {
        for (int i = vertexSize() - 1; i >= 0; i--) {
            if (item == mVertexes[i].info) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 深度优先搜索遍历图的递归实现
     */
    private void dfs(boolean[] visited, int i, ArrayList<T> list) {
        visited[i] = true;
        list.add(mVertexes[i].info);

        AdjacentNode edge = mVertexes[i].firstEdge;
        while (edge != null) {
            if (!visited[edge.vexIdx]) {
                dfs(visited, edge.vexIdx, list);
            }
            edge = edge.nextEdge;
        }
    }

    /**
     * 深度优先搜索遍历图
     */
    @SuppressWarnings("unchecked")
    public T[] dfs() {
        ArrayList<T> list = new ArrayList<>();

        int vLen = vertexSize();
        boolean[] visited = new boolean[vLen];

        for (int i = 0; i < vLen; i++) {
            if (!visited[i]) {
                dfs(visited, i, list);
            }
        }

        System.out.println("DFS: " + list.toString());
        return (T[])list.toArray();
    }

    /**
     * 广度优先搜索遍历图
     */
    @SuppressWarnings("unchecked")
    public T[] bfs() {
        ArrayList<T> list = new ArrayList<>();

        int vLen = vertexSize();
        int head = 0;
        int rear = 0;
        // 辅组队列
        int[] queue = new int[vLen];
        // 顶点访问标记
        boolean[] visited = new boolean[vLen];

        for (int i = 0; i < vLen; i++) {
            if (!visited[i]) {
                visited[i] = true;
                list.add(mVertexes[i].info);
                // 入列
                queue[rear++] = i;
            }
            while (head != rear) {
                // 出列
                int j = queue[head++];
                AdjacentNode node = mVertexes[j].firstEdge;
                while (node != null) {
                    int k = node.vexIdx;
                    if (!visited[k]) {
                        visited[k] = true;
                        list.add(mVertexes[k].info);
                        // 入列
                        queue[rear++] = k;
                    }
                    node = node.nextEdge;
                }
            }
        }

        System.out.println("BFS: " + list.toString());
        return (T[])list.toArray();
    }

    public void dump() {
        System.out.printf("List Directed Graph:\n");
        for (int i = 0; i < vertexSize(); i++) {
            System.out.printf("%d(%c): ", i, mVertexes[i].info);
            AdjacentNode node = mVertexes[i].firstEdge;
            while (node != null) {
                System.out.printf("%d(%c) ", node.vexIdx, mVertexes[node.vexIdx].info);
                node = node.nextEdge;
            }
            System.out.printf("\n");
        }
    }

    /**
     * 获取顶点数量
     * @return
     */
    public int vertexSize() {
        return mVertexes.length;
    }

    /**
     * 获取顶点数组中序号为v的顶点中存储的数组
     */
    public T vertexInfo(int v) {
        if (v < 0 || v > mVertexes.length - 1) {
            throw new IndexOutOfBoundsException();
        }
        return mVertexes[v].info;
    }

    /**
     * 获取顶点数组中序号为v的顶点的所有连接顶点的序号
     */
    public int[] adjacentVertexIndexes(int v) {
        ArrayList<Integer> list = new ArrayList<>();

        AdjacentNode node = mVertexes[v].firstEdge;
        while (node != null) {
            list.add(node.vexIdx);
            node = node.nextEdge;
        }

        int[] ret = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    /**
     * 获取当前有向图的反向图
     */
    public DirectedGraph<T> reverse() {
        // 顶点数量
        int vLen = vertexSize();

        // 顶点赋初值
        @SuppressWarnings("unchecked")
        VertexNode<T>[] vertexes = (VertexNode<T>[])new VertexNode[vLen];
        for (int i = 0; i < vLen; i++) {
            VertexNode<T> vertex = new VertexNode<>();;
            vertex.info = mVertexes[i].info;
            vertexes[i] = vertex;
        }

        for (int i = 0; i < vLen; i++) {
            AdjacentNode node = mVertexes[i].firstEdge;
            while (node != null) {
                int vexId = node.vexIdx;
                AdjacentNode edge = new AdjacentNode();
                edge.vexIdx = i;
                if (vertexes[vexId].firstEdge == null) {
                    vertexes[vexId].firstEdge = edge;
                } else {
                    linkLast(vertexes[vexId].firstEdge, edge);
                }
                node = node.nextEdge;
            }
        }

        return new DirectedGraph<T>(vertexes);
    }

/****************************************************************************************************************
 Kosaraju算法求解有向图的强连通分量：
 1. 需要保证被指向的强连通分量的至少一个顶点排在该连通分量的所有顶点前面,Kosaraju给出的解决办法:
 (1)对原图取反;
 (2)从反向图的任意节点开始, 进行DFS的逆后序遍历, 逆后序得到的顺序一定满足我们的要求.
 2. DFS的逆后序遍历:
 (1)若当前顶点未访问,先遍历完与当前顶点邻接且未被访问的所有其它顶点
 (2)将当前顶点加入栈中,最后栈中从栈顶到栈底的顺序就是我们需要的顶点顺序。
****************************************************************************************************************/
    public static class StronglyConnectedComponent<T> {
        private DirectedGraph<T> mGraph;

        // 标识顶点是否已经被访问过
        private boolean[] mVisited;
        // 给每个顶点标识一个id，id相同的顶点构成一个强连通分量
        private int[] mIds;
        // 强连通分量的个数
        private int mCount;

        public StronglyConnectedComponent(DirectedGraph<T> graph) {
            this.mGraph = graph;

            int vLen = graph.vertexSize();
            mVisited = new boolean[vLen];
            mIds = new int[vLen];
            mCount = 0;

            // 获取有向图的反向图
            DirectedGraph<T> reversedGraph = graph.reverse();
            // 反向图DFS的逆后序，可以从任意顶点开始
            LinkedList<Integer> stack = reversePostOrder(reversedGraph, 0);
            while (!stack.isEmpty()) {
                int v = stack.pop();
                if (!mVisited[v]) {
                    dfs(graph, v);
                    mCount++;
                }
            }
        }

        private void dfs(DirectedGraph<T> graph, int v) {
            if (!mVisited[v]) {
                mVisited[v] = true;
                mIds[v] = mCount;
                for (int w : graph.adjacentVertexIndexes(v)) {
                    dfs(graph, w);
                }
            }
        }

        private LinkedList<Integer> reversePostOrder(DirectedGraph<T> graph, int v) {
            LinkedList<Integer> stack = new LinkedList<>();
            int vLen = graph.vertexSize();
            boolean[] visited = new boolean[vLen];
            for (int i = 0; i < vLen; i++) {
                dfsForReversePostOrder(graph, visited, stack, i);
            }
            return stack;
        }

        private void dfsForReversePostOrder(DirectedGraph<T> graph,
                boolean[] visited, LinkedList<Integer> stack, int v) {
            if (!visited[v]) {
                visited[v] = true;
                for (int w : graph.adjacentVertexIndexes(v)) {
                    dfsForReversePostOrder(graph, visited, stack, w);
                }
                stack.push(v);
            }
        }

        /**
         * 获取有向图的强连通分量的数量
         */
        public int count() {
            return this.mCount;
        }

        /**
         * 获取顶点数组中序号为v的顶点的所有邻接点的序号
         */
        public int[] allConnected(int v){
            ArrayList<Integer> list = new ArrayList<>();
            int id = mIds[v];
            for (int i = 0; i < this.mGraph.vertexSize(); i++) {
                if (mIds[i] == id) {
                    list.add(i);
                }
            }
            int[] ret = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ret[i] = list.get(i);
            }
            list.clear();
            list = null;
            return ret;
        }
    }

    public static void main(String[] args) {
        Character[] vexs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        Character[][] edges = new Character[][] { { 'A', 'B' }, { 'B', 'C' },
            { 'B', 'E' }, { 'B', 'F' }, { 'C', 'E' }, { 'D', 'C' },
            { 'E', 'B' }, { 'E', 'D' }, { 'F', 'G' } };

        DirectedGraph<Character> graph = new DirectedGraph<>(vexs, edges);
        graph.dump();
        graph.dfs();
        graph.bfs();
        System.out.println();

        StronglyConnectedComponent<Character> scc = new StronglyConnectedComponent<>(graph);
        System.out.println("numder of the strongly connected component in current directed graph is " + (scc.count()));
        int[] idxes = scc.allConnected(graph.index('B'));
        Character[] chs = new Character[idxes.length];
        for (int i = 0 ; i < idxes.length; i++) {
            chs[i] = graph.vertexInfo(idxes[i]);
        }
        System.out.println("all strongly connected vertexes for vertex B in current directed graph are " + (Arrays.toString(chs)));

        /**
        List Directed Graph:
        0(A): 1(B) 
        1(B): 2(C) 4(E) 5(F) 
        2(C): 4(E) 
        3(D): 2(C) 
        4(E): 1(B) 3(D) 
        5(F): 6(G) 
        6(G): 
        DFS: [A, B, C, E, D, F, G]
        BFS: [A, B, C, E, F, D, G]

        numder of the strongly connected component in current directed graph is 4
        all strongly connected vertexes for vertex B are [B, C, D, E]
         */
    }
}
