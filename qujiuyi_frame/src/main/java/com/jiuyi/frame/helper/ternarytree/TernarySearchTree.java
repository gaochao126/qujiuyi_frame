package com.jiuyi.frame.helper.ternarytree;

import java.util.ArrayList;

/**
 * 
 * Author: xutaoyang Date: 下午4:26:35
 * 
 * Copyright @ 2015 重庆玖壹健康管理有限公司
 * 
 */
public class TernarySearchTree {
	private TSTNode root;
	private ArrayList<String> al;

	public TernarySearchTree() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void makeEmpty() {
		root = null;
	}

	public void insert(String word) {
		root = insert(root, word.toCharArray(), 0);
	}

	public TSTNode insert(TSTNode r, char[] word, int ptr) {
		if (r == null)
			r = new TSTNode(word[ptr]);

		if (word[ptr] < r.data)
			r.left = insert(r.left, word, ptr);
		else if (word[ptr] > r.data)
			r.right = insert(r.right, word, ptr);
		else {
			if (ptr + 1 < word.length)
				r.middle = insert(r.middle, word, ptr + 1);
			else
				r.isEnd = true;
		}
		return r;
	}

	public void delete(String word) {
		delete(root, word.toCharArray(), 0);
	}

	private void delete(TSTNode r, char[] word, int ptr) {
		if (r == null)
			return;

		if (word[ptr] < r.data)
			delete(r.left, word, ptr);
		else if (word[ptr] > r.data)
			delete(r.right, word, ptr);
		else {
			/** to delete a word just make isEnd false **/
			if (r.isEnd && ptr == word.length - 1)
				r.isEnd = false;

			else if (ptr + 1 < word.length)
				delete(r.middle, word, ptr + 1);
		}
	}

	public boolean search(String word) {
		return search(root, word.toCharArray(), 0);
	}

	private boolean search(TSTNode r, char[] word, int ptr) {
		if (r == null)
			return false;

		if (word[ptr] < r.data)
			return search(r.left, word, ptr);
		else if (word[ptr] > r.data)
			return search(r.right, word, ptr);
		else {
			if (r.isEnd && ptr == word.length - 1)
				return true;
			else if (ptr == word.length - 1)
				return false;
			else
				return search(r.middle, word, ptr + 1);
		}
	}

	public String toString() {
		al = new ArrayList<String>();
		traverse(root, "");
		return "\nTernary Search Tree : " + al;
	}

	private void traverse(TSTNode r, String str) {
		if (r != null) {
			traverse(r.left, str);

			str = str + r.data;
			if (r.isEnd)
				al.add(str);

			traverse(r.middle, str);
			str = str.substring(0, str.length() - 1);

			traverse(r.right, str);
		}
	}
}

class TSTNode {
	char data;
	boolean isEnd;
	TSTNode left, middle, right;

	public TSTNode(char data) {
		this.data = data;
		this.isEnd = false;
		this.left = null;
		this.middle = null;
		this.right = null;
	}
}
