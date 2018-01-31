package com.tuhailong.graph;

/**
 * 无向图的邻接矩阵表示
 * 
 * @author tuhailong
 */
public class MatrixUndirectedGraph {
    // 顶点集合
    private char[] mVertexes;
    // 邻接矩阵
    private int[][] mMatrix;

    MatrixUndirectedGraph(char[] vexs, char[][] edges) {
        // 获取顶点数量
        int vLen = vexs.length;
        // 获取边数量
        int eLen = edges.length;

        // 顶点赋值
        mVertexes = new char[vLen];
        for (int i = 0; i < vLen; i++) {
            mVertexes[i] = vexs[i];
        }

        // 边赋初始值
        mMatrix = new int[vLen][vLen];
        for (int i = 0; i < eLen; i++) {
            // 边edges[i]的起始顶点
            int sp = index(edges[i][0]);
            // 边edges[i]的终止顶点
            int ep = index(edges[i][1]);
            if (sp == -1 || ep == -1) {
                continue;
            }
            // 在无向图中，mMatrix[sp][ep]==1表示顶点mVertexes[sp]与顶点mVertexes[ep]是联通的
            mMatrix[sp][ep] = 1;
            mMatrix[ep][sp] = 1;
        }
    }

    /**
     * 返回ch在mVertexes中的位置
     */
    private int index(char ch) {
        for (int i = mVertexes.length - 1; i >= 0; i--) {
            if (ch == mVertexes[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回顶点v的第一个邻接顶点的索引，失败则返回-1
     */
    private int firstVertex(int v) {
        if (v < 0 || v > mVertexes.length - 1) {
            return -1;
        }
        for (int i = 0; i < mVertexes.length; i++) {
            if (mMatrix[v][i] == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回顶点v相对于w的下一个邻接顶点的索引，失败则返回-1
     */
    private int nextVertex(int v, int w) {
        if (v < 0 || v > mVertexes.length - 1) {
            return -1;
        }
        if (w < 0 || w > mVertexes.length - 1) {
            return -1;
        }
        for (int i = w + 1; i < mVertexes.length; i++) {
            if (mMatrix[v][i] == 1) {
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
        System.out.printf("%c ", mVertexes[i]);
        // 遍历该顶点的所有邻接顶点:若是没有访问过,那么继续往下走
        for (int w = firstVertex(i); w >= 0; w = nextVertex(i, w)) {
            if (!visited[w]) {
                dfs(w, visited);
            }
        }
    }

    /**
     * 深度优先搜索遍历图
     */
    public void dfs() {
        int vLen = mVertexes.length;
        boolean[] visited = new boolean[vLen];
        for (int i = 0; i < vLen; i++) {
            visited[i] = false;
        }

        System.out.printf("DFS: ");
        for (int i = 0; i < vLen; i++) {
            if (!visited[i]) {
                dfs(i, visited);
            }
        }
        System.out.printf("\n");
    }

    public void dump() {
        System.out.printf("Martix Undirected Graph:\n");
        System.out.print("    ");
        for (int i = 0; i < mVertexes.length; i++)
            System.out.printf("%c ", mVertexes[i]);
        System.out.printf("\n");
        System.out.println("------------------");

        for (int i = 0; i < mVertexes.length; i++) {
            System.out.printf("%c | ", mVertexes[i]);
            for (int j = 0; j < mVertexes.length; j++)
                System.out.printf("%d ", mMatrix[i][j]);
            System.out.printf("\n");
        }
    }

    public static void main(String[] args) {
        char[] vexs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        char[][] edges = new char[][] { { 'A', 'C' }, { 'A', 'D' },{ 'A', 'F' },
            { 'B', 'C' }, { 'C', 'D' }, { 'E', 'G' }, { 'F', 'G' } };

        MatrixUndirectedGraph graph = new MatrixUndirectedGraph(vexs, edges);
        graph.dump();
        System.out.println();
        graph.dfs();
        /**
            Martix Undirected Graph:
                A B C D E F G 
            ------------------
            A | 0 0 1 1 0 1 0 
            B | 0 0 1 0 0 0 0 
            C | 1 1 0 1 0 0 0 
            D | 1 0 1 0 0 0 0 
            E | 0 0 0 0 0 0 1 
            F | 1 0 0 0 0 0 1 
            G | 0 0 0 0 1 1 0 

            DFS: A C B D F G E 
        */
    }
}
