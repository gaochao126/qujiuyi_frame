package com.jiuyi.frame.helper.ternarytree;

/**
 * 
 * Author: xutaoyang Date: 下午3:12:44
 * 
 * Copyright @ 2015 重庆玖壹健康管理有限公司
 * 
 */
public class TernaryTree {
	private Node m_root = null;

	private Node addStr(String s, int pos) {
		Node node = new Node(s.charAt(pos), false);
		if (s.charAt(pos) < node.m_char) {
			node.m_left = addStr(s, pos);
		} else if (s.charAt(pos) > node.m_char) {
			node.m_right = addStr(s, pos);
		} else {
			if (pos + 1 == s.length()) {
				node.m_wordEnd = true;
			} else {
				node.m_center = addStr(s, pos + 1);
			}
		}
		return node;
	}

	public void addStr(String s) {
		if (s == null || s.equals("")) {
			return;
		}
		m_root = addStr(s, 0);
	}

	public boolean containsStr(String s) {
		if (s == null || s.equals("")) {
			return false;
		}
		int pos = 0;
		Node node = m_root;
		while (node != null) {
			if (s.charAt(pos) < node.m_char) {
				node = node.m_left;
			} else if (s.charAt(pos) > node.m_char) {
				node = node.m_right;
			} else {
				if (++pos == s.length()) {
					return node.m_wordEnd;
				}
				node = node.m_center;
			}
		}
		return false;
	}

	public class Node {
		public char m_char;
		public Node m_left, m_center, m_right;
		public boolean m_wordEnd;

		public Node(char ch, boolean wordEnd) {
			this.m_char = ch;
			this.m_wordEnd = wordEnd;
		}
	}
}