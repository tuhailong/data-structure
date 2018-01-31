package com.tuhailong.graph;

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

        // 边赋值
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

    private int index(char ch) {
        for (int i = mVertexes.length - 1; i >= 0; i--) {
            if (ch == mVertexes[i]) {
                return i;
            }
        }
        return -1;
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

        MatrixUndirectedGraph gragh = new MatrixUndirectedGraph(vexs, edges);
        gragh.dump();
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
        */
    }
}
