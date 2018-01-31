package com.tuhailong.graph;

/**
 * 无向图的邻接链表表示
 * @author tuhailong
 *
 */
public class ListUndirectedGraph {
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

    ListUndirectedGraph(char[] vexs, char[][] edges) {
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

            // 初始化上sNode
            EdgeNode eNode = new EdgeNode();
            eNode.vexIdx = sp;
            // 将eNode链接到"ep所在链表的末尾"
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

    public void dump() {
        System.out.printf("List Undirected Graph:\n");
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
        char[][] edges = new char[][] { { 'A', 'C' }, { 'A', 'D' },{ 'A', 'F' },
            { 'B', 'C' }, { 'C', 'D' }, { 'E', 'G' }, { 'F', 'G' } };

        ListUndirectedGraph gragh = new ListUndirectedGraph(vexs, edges);
        gragh.dump();
        /**
            List Undirected Graph:
            0(A): 2(C) 3(D) 5(F) 
            1(B): 2(C) 
            2(C): 0(A) 1(B) 3(D) 
            3(D): 0(A) 2(C) 
            4(E): 6(G) 
            5(F): 0(A) 6(G) 
            6(G): 4(E) 5(F)
         */
    }
}
