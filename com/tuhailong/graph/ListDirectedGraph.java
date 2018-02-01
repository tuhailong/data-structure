package com.tuhailong.graph;

/**
 * 有向图的邻接链表表示
 * @author tuhailong
 */
public class ListDirectedGraph {
    // 邻接链表中表对应的链表的结点
    private class EdgeNode {
        // 该边所指向的顶点的位置
        int vexIdx;
        // 指向下一条边的指针
        EdgeNode nextEdge;
    }

    // 邻接链表中表的顶点
    private class VertexNode {
        // 顶点信息
        char info;
        // 指向第一条依附该顶点的边
        EdgeNode firstEdge;
    }

    // 顶点集合
    private VertexNode[] mVertexes;

    ListDirectedGraph(char[] vexs, char[][] edges) {
        // 顶点数量
        int vLen = vexs.length;
        // 边的数量
        int eLen = edges.length;

        // 顶点赋初值
        mVertexes = new VertexNode[vLen];
        for (int i = 0; i < vLen; i++) {
            mVertexes[i] = new VertexNode();
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
            EdgeNode sNode = new EdgeNode();
            sNode.vexIdx = ep;
            // 将sNode链接到"sp所在链表的末尾"
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
    private void linkLast(EdgeNode list, EdgeNode node) {
        EdgeNode p = list;
        while (p.nextEdge != null) {
            p = p.nextEdge;
        }
        p.nextEdge = node;
    }

    /**
     * 返回ch在mVertexes中的位置
     */
    private int index(char ch) {
        for (int i = mVertexes.length - 1; i >= 0; i--) {
            if (ch == mVertexes[i].info) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 深度优先搜索遍历图的递归实现
     */
    private void dfs(int i, boolean[] visited) {
        visited[i] = true;

        System.out.printf("%c ", mVertexes[i].info);
        EdgeNode edge = mVertexes[i].firstEdge;
        while (edge != null) {
            if (!visited[edge.vexIdx]) {
                dfs(edge.vexIdx, visited);
            }
            edge = edge.nextEdge;
        }
    }

    /**
     * 深度优先搜索遍历图
     */
    public void dfs() {
        int vLen = mVertexes.length;
        boolean[] visited = new boolean[vLen];

        System.out.printf("DFS: ");
        for (int i = 0; i < vLen; i++) {
            if (!visited[i]) {
                dfs(i, visited);
            }
        }
        System.out.printf("\n");
    }

    /**
     * 广度优先搜索遍历图
     */
    public void bfs() {
        int vLen = mVertexes.length;
        int head = 0;
        int rear = 0;
        // 辅组队列
        int[] queue = new int[vLen];
        // 顶点访问标记
        boolean[] visited = new boolean[vLen];

        System.out.printf("BFS: ");
        for (int i = 0; i < vLen; i++) {
            if (!visited[i]) {
                visited[i] = true;
                System.out.printf("%c ", mVertexes[i].info);
                // 入列
                queue[rear++] = i;
            }

            while (head != rear) {
                // 出列
                int j = queue[head++];
                EdgeNode node = mVertexes[j].firstEdge;
                while (node != null) {
                    int k = node.vexIdx;
                    if (!visited[k]) {
                        visited[k] = true;
                        System.out.printf("%c ", mVertexes[k].info);
                        // 入列
                        queue[rear++] = k;
                    }
                    node = node.nextEdge;
                }
            }
        }
        System.out.printf("\n");
    }

    public void dump() {
        System.out.printf("List Directed Graph:\n");
        for (int i = 0; i < mVertexes.length; i++) {
            System.out.printf("%d(%c): ", i, mVertexes[i].info);
            EdgeNode node = mVertexes[i].firstEdge;
            while (node != null) {
                System.out.printf("%d(%c) ", node.vexIdx, mVertexes[node.vexIdx].info);
                node = node.nextEdge;
            }
            System.out.printf("\n");
        }
    }

    public static void main(String[] args) {
        char[] vexs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        char[][] edges = new char[][] { { 'A', 'B' }, { 'B', 'C' },
            { 'B', 'E' }, { 'B', 'F' }, { 'C', 'E' }, { 'D', 'C' },
            { 'E', 'B' }, { 'E', 'D' }, { 'F', 'G' } };

        ListDirectedGraph gragh = new ListDirectedGraph(vexs, edges);
        gragh.dump();
        System.out.println();
        gragh.dfs();
        System.out.println();
        gragh.bfs();
        /**
        List Directed Graph:
        0(A): 1(B) 
        1(B): 2(C) 4(E) 5(F) 
        2(C): 4(E) 
        3(D): 2(C) 
        4(E): 1(B) 3(D) 
        5(F): 6(G) 
        6(G): 

        DFS: A B C E D F G 

        BFS: A B C E F D G 
         */
    }
}
