package com.tuhailong.graph;

import java.util.ArrayList;

/**
 * 有向图的邻接链表表示
 * @author tuhailong
 */
public class ListDirectedGraph<T> {

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
    ListDirectedGraph(T[] vexs, T[][] edges) {
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
    private int index(T item) {
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

    public void dump() {
        System.out.printf("List Directed Graph:\n");
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

    public static void main(String[] args) {
        Character[] vexs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        Character[][] edges = new Character[][] { { 'A', 'B' }, { 'B', 'C' },
            { 'B', 'E' }, { 'B', 'F' }, { 'C', 'E' }, { 'D', 'C' },
            { 'E', 'B' }, { 'E', 'D' }, { 'F', 'G' } };

        ListDirectedGraph<Character> graph = new ListDirectedGraph<Character>(vexs, edges);
        graph.dump();
        graph.dfs();
        graph.bfs();
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
         */
    }
}
