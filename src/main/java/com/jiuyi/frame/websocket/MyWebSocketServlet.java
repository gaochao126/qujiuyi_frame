package com.jiuyi.frame.websocket;

//@ServerEndpoint(value = "/ws/chat")
public class MyWebSocketServlet {

//	/**
//	 * 连接对象集合
//	 */
//	private static final Set<MyWebSocketServlet> connections = new CopyOnWriteArraySet<MyWebSocketServlet>();
//
//	private String nickName;
//
//	/**
//	 * WebSocket Session
//	 */
//	private Session session;
//
//	/**
//	 * 打开连接
//	 * 
//	 * @param session
//	 * @param nickName
//	 */
//	@OnOpen
//	public void onOpen(Session session) {
//		this.session = session;
//		this.nickName = "name";
//		connections.add(this);
//		System.out.println("sessionId:" + session.getId());
//		String message = String.format("System> %s %s", this.nickName, " has joined.");
//		MyWebSocketServlet.broadCast(message);
//	}
//
//	/**
//	 * 关闭连接
//	 */
//	@OnClose
//	public void onClose() {
//		connections.remove(this);
//		String message = String.format("System> %s, %s", this.nickName, " has disconnection.");
//		MyWebSocketServlet.broadCast(message);
//	}
//
//	/**
//	 * 接收信息
//	 * 
//	 * @param message
//	 * @param nickName
//	 */
//	@OnMessage
//	public void onMessage(String message) {
//		System.out.println("message: " + message);
//		MyWebSocketServlet.broadCast(nickName + ">" + message);
//	}
//	
//	
//
//	/**
//	 * 错误信息响应
//	 * 
//	 * @param throwable
//	 */
//	@OnError
//	public void onError(Throwable throwable) {
//		System.out.println(throwable.getMessage());
//	}
//
//	/**
//	 * 发送或广播信息
//	 * 
//	 * @param message
//	 */
//	private static void broadCast(String message) {
//		for (MyWebSocketServlet chat : connections) {
//			try {
//				synchronized (chat) {
//					chat.session.getBasicRemote().sendText(message);
//				}
//			} catch (IOException e) {
//				connections.remove(chat);
//				try {
//					chat.session.close();
//				} catch (IOException e1) {
//				}
//				MyWebSocketServlet.broadCast(String.format("System> %s %s", chat.nickName, " has bean disconnection."));
//			}
//		}
//	}
}
