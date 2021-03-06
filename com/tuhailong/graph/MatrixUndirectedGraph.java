package com.tuhailong.graph;

import java.util.ArrayList;

/**
 * 无向图的邻接矩阵表示
 * 
 * @author tuhailong
 */
public class MatrixUndirectedGraph<T> {
    // 顶点数组
    private T[] mVertexes;
    // 邻接矩阵
    private int[][] mMatrix;

    @SuppressWarnings("unchecked")
    MatrixUndirectedGraph(T[] vexs, T[][] edges) {
        // 获取顶点数量
        int vLen = vexs.length;
        // 获取边数量
        int eLen = edges.length;

        // 顶点赋值
        mVertexes = (T[])new Object[vLen];
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
            // 在无向图中，mMatrix[sp][ep]==1表示顶点mVertexes[sp]与顶点mVertexes[ep]是连通的
            mMatrix[sp][ep] = 1;
            mMatrix[ep][sp] = 1;
        }
    }

    /**
     * 返回item在mVertexes中的位置
     */
    private int index(T item) {
        for (int i = mVertexes.length - 1; i >= 0; i--) {
            if (item == mVertexes[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回顶点v的第一个邻接顶点的索引，失败则返回-1
     */
    private int firstVertex(int v) {
        int vLen = mVertexes.length;
        if (v < 0 || v > vLen - 1) {
            return -1;
        }
        for (int i = 0; i < vLen; i++) {
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
        int vLen = mVertexes.length;
        if (v < 0 || v > vLen - 1) {
            return -1;
        }
        if (w < 0 || w > vLen - 1) {
            return -1;
        }
        for (int i = w + 1; i < vLen; i++) {
            if (mMatrix[v][i] == 1) {
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
        list.add(mVertexes[i]);
        // 遍历该顶点的所有邻接顶点:若是没有访问过,那么继续往下走
        for (int w = firstVertex(i); w >= 0; w = nextVertex(i, w)) {
            if (!visited[w]) {
                dfs(visited, w, list);
            }
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
                list.add(mVertexes[i]);
                // 入列
                queue[rear++] = i;
            }

            while (head != rear) {
                // 出列
                int j = queue[head++];
                //k为访问的邻接顶点
                for (int k = firstVertex(j); k >= 0; k = nextVertex(j, k)) {
                    if (!visited[k]) {
                        visited[k] = true;
                        list.add(mVertexes[k]);
                        // 入列
                        queue[rear++] = k;
                    }
                }
            }
        }

        System.out.println("BFS: " + list.toString());
        return (T[])list.toArray();
    }

    public boolean isConnected() {
        int count = 0;

        int vLen = mVertexes.length;
        // 顶点访问标记
        boolean[] visited = new boolean[vLen];
        // 辅组队列
        int[] queue = new int[vLen];
        int head = 0;
        int tail = 0;

        // 选择顶点数组的首元素位置入列
        queue[tail++] = 0;

        while (head != tail) {
            // 出列
            int i = queue[head++];
            visited[i] = true;
            count += 1;
            for (int j = firstVertex(i); j >= 0; j = nextVertex(i, j)) {
                if (!visited[j]) {
                    visited[j] = true;
                    // 顶点数组j位置入列
                    queue[tail++] = j;
                }
            }
        }

        System.out.println("isConnected=" + (count == vLen));
        return count == vLen;
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
        Character[] vexs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        Character[][] edges = new Character[][] { { 'A', 'C' }, { 'A', 'D' },{ 'A', 'F' },
            { 'B', 'C' }, { 'C', 'D' }, { 'E', 'G' }, { 'F', 'G' } };

        MatrixUndirectedGraph<Character> graph = new MatrixUndirectedGraph<Character>(vexs, edges);
        graph.dump();
        graph.dfs();
        graph.bfs();
        graph.isConnected();
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
        DFS: [A, C, B, D, F, G, E]
        BFS: [A, C, D, F, B, G, E]
        isConnected=true
        */
    }
}