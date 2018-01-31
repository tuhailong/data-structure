package com.tuhailong.graph;

/**
 * ����ͼ���ڽ������ʾ
 * @author tuhailong
 */
public class ListDirectedGraph {
    // �ڽ������б��Ӧ������Ľ��
    private class EdgeNode {
        // �ñ���ָ��Ķ����λ��
        int vexIdx;
        // ָ����һ���ߵ�ָ��
        EdgeNode nextEdge;
    }

    // �ڽ������б�Ķ���
    private class VertexNode {
        // ������Ϣ
        char info;
        // ָ���һ�������ö���ı�
        EdgeNode firstEdge;
    }

    // ���㼯��
    private VertexNode[] mVertexes;

    ListDirectedGraph(char[] vexs, char[][] edges) {
        // ��������
        int vLen = vexs.length;
        // �ߵ�����
        int eLen = edges.length;

        // ���㸳��ֵ
        mVertexes = new VertexNode[vLen];
        for (int i = 0; i < vLen; i++) {
            mVertexes[i] = new VertexNode();
            mVertexes[i].info = vexs[i];
            mVertexes[i].firstEdge = null;
        }

        for (int i = 0; i < eLen; i++) {
            // ��edges[i]����ʼ������mVertexes[i]��λ��
            int sp = index(edges[i][0]);
            // ��edges[i]����ֹ������mVertexes[i]��λ��
            int ep = index(edges[i][1]);
            if (sp == -1 || ep == -1) {
                continue;
            }
            // ��ʼ����sNode
            EdgeNode sNode = new EdgeNode();
            sNode.vexIdx = ep;
            // ��sNode���ӵ�"sp���������ĩβ"
            if (mVertexes[sp].firstEdge == null) {
                mVertexes[sp].firstEdge = sNode;
            } else {
                linkLast(mVertexes[sp].firstEdge, sNode);
            }
        }
    }

    /**
     * ��node�ڵ����ӵ�list��β��
     */
    private void linkLast(EdgeNode list, EdgeNode node) {
        EdgeNode p = list;
        while (p.nextEdge != null) {
            p = p.nextEdge;
        }
        p.nextEdge = node;
    }

    /**
     * ����ch��mVertexes�е�λ��
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
        /**
            List Directed Graph:
            0(A): 1(B) 
            1(B): 2(C) 4(E) 5(F) 
            2(C): 4(E) 
            3(D): 2(C) 
            4(E): 1(B) 3(D) 
            5(F): 6(G) 
            6(G): 
         */
    }
}
