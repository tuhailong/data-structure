package com.tuhailong.graph;

import java.util.ArrayList;

/**
 * 无向图的邻接链表表示
 * @author tuhailong
 */
public class UndirectedGraph<T> {
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
    UndirectedGraph(T[] vexs, T[][] edges) {
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

            // 初始化上sNode
            AdjacentNode eNode = new AdjacentNode();
            eNode.vexIdx = sp;
            // 将eNode链接到"顶点ep所指向的链表的末尾"
            if (mVertexes[ep].firstEdge == null) {
                mVertexes[ep].firstEdge = eNode;
            } else {
                linkLast(mVertexes[ep].firstEdge, eNode);
            }
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
        for (int i = mVertexes.length - 1; i >= 0; i--) {
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

        int vLen = mVertexes.length;
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

        int vLen = mVertexes.length;
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

   /**
     * 判断无向图是否有环
     */
    public boolean hasCycle() {
        boolean[] hasCycle = new boolean[1];
        int vLen = mVertexes.length;
        boolean[] visited = new boolean[vLen];
        for (int i = 0; i < vLen; i++) {
            if (!visited[i]) {
                // 1.刚开始没有顶点被访问过，当前正访问和上一个被访问的顶点都设置为起点i;
                // 2.当dfsForCycle被递归调用一次后，当前正访问的参数v是i的一个邻接点，而上一个被访问的参数u是i.
                dfsForCycle(visited, i, i, hasCycle);
            }
        }
        System.out.println("current graph does " + (hasCycle[0] ? "" : "not ") + "have cycle");
        return hasCycle[0];
    }

    // v表示当前正访问的顶点，u表示上一个访问的顶点
    private void dfsForCycle(boolean[] visited, int v, int u, boolean[] hasCycle) {
        // 设置(顶点数组中序号为v的顶点)顶点v的已访问标记
        visited[v] = true;
        // 遍历(顶点数组中序号为v的顶点)顶点v的所有邻接顶点
        AdjacentNode node = mVertexes[v].firstEdge;
        while (node != null) {
            int w = node.vexIdx;
            if (!visited[w]) {
                dfsForCycle(visited, w, v, hasCycle);
            } else if (w != u) {
                hasCycle[0] = true;
                break;
            }
            node = node.nextEdge;
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
     * 获取顶点数组中序号为v的顶点的所有连接顶点
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

    public void dump() {
        System.out.printf("List Undirected Graph:\n");
        for (int i = 0; i < mVertexes.length; i++) {
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
     * 1.若图的任意顶点都存在一条路径达到任意一个顶点，则称该图是连通图；
     * 2.连通分量是指图中所有极大连通子图；
     * 3.若图的所有顶点都是分散的，那么连通分量的数量（只有一个顶点）就是图的顶点数量；
     * 4.连通分量的数量范围为[1, 图的顶点数量]
     */
    public static class ConnectedComponent<T> {
        // 标识顶点是否已经被访问过
        private boolean[] mVisited;
        // 给每个顶点标识一个id，id相同的顶点构成一个联通分量
        private int[] mIds;
        // 连通分量的个数
        private int mCount;

        public ConnectedComponent(UndirectedGraph<T> graph) {
            int vLen = graph.vertexSize();
            mVisited = new boolean[vLen];
            mIds = new int[vLen];
            mCount = 0;
            for (int i = 0; i < vLen; i++) {
                if (!mVisited[i]) {
                    // 一次dfs调用就是一个连通分量
                    dfs(graph, i);
                    mCount += 1;
                }
            }
        }

        private void dfs(UndirectedGraph<T> graph, int v) {
            // 标识刚访问到的顶点v
            mVisited[v] = true;
            mIds[v] = mCount;
            // 遍历顶点v的所有没有被访问过的邻接点
            for (int w : graph.adjacentVertexIndexes(v)) {
                if (!mVisited[w]) {
                    dfs(graph, w);
                }
            }
        }

        /**
         * 顶点数组中序号为v的顶点的id
         */
        public int id(int v) {
            return mIds[v];
        }

        /**
         * 顶点数组中序号为v的顶点和序号为w的顶点是否是连通的
         */
        public boolean connected(int v, int w) {
            return id(v) == id(w);
        }

        /**
         * 连通分量的数量
         */
        public int count() {
            return mCount;
        }

        /**
         * 该无向图是否是连通图
         */
        public boolean isAConnectedGraph() {
            return count() == 1;
        }
    }

    public static void main(String[] args) {
        Character[] vexs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        Character[][] edges = new Character[][] { { 'A', 'C' }, { 'A', 'D' },{ 'A', 'F' },
            { 'B', 'C' }, { 'C', 'D' }, { 'E', 'G' }, { 'F', 'G' } };

        UndirectedGraph<Character> graph = new UndirectedGraph<Character>(vexs, edges);
        graph.dump();
        graph.dfs();
        graph.bfs();
        graph.hasCycle();

        ConnectedComponent<Character> cc = new ConnectedComponent<>(graph);
        boolean connected = cc.connected(graph.index('A'), graph.index('G'));
        System.out.println("vertext A and G is connectes is " + connected);
        System.out.println("connected component count is " + cc.count());
        System.out.println("current graph is " + (cc.isAConnectedGraph() ? "" : "not ") + "a connected graph");
        /**
        List Undirected Graph:
        0(A): 2(C) 3(D) 5(F) 
        1(B): 2(C) 
        2(C): 0(A) 1(B) 3(D) 
        3(D): 0(A) 2(C) 
        4(E): 6(G) 
        5(F): 0(A) 6(G) 
        6(G): 4(E) 5(F) 
        DFS: [A, C, B, D, F, G, E]
        BFS: [A, C, D, F, B, G, E]
        current graph does have cycle
        vertext A and G is connectes is true
        connected component count is 1
        current graph is a connected graph
         */
    }
}
