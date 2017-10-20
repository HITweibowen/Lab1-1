package liu.zhang;

import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import static java.lang.System.out;

/**
 * 功能: 元素对
 */
class Pair<A, B>{
	private A first;
	private B second;

	public Pair() {
		first=null;
		second=null;
	}
	public Pair(A first, B second) {
		this.first=first;
		this.second=second;
	}

	public A getFirst() {
		return first;
	}
	public B getSecond() {
		return second;
	}

	public void setFirst(A newValue) {
		first = newValue;
	}
	public void setSecond(B newValue) {
		second =newValue;
	}
}

/**
 * 功能: 选择文件
 */
class MyFilter extends FileFilter {
    private String ext;

    public MyFilter(String extString) {
    	this.ext = extString;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
        	return true;
        }
        String extension = getExtension(f);
        if (extension.toLowerCase().equals(this.ext.toLowerCase())) {
            return true;
        }
        return false;
    }

    public String getDescription() {
    	return this.ext.toUpperCase();
    }

    private String getExtension(File f) {
        String name = f.getName();
        int index = name.lastIndexOf('.');
        if (index == -1){
            return "";
        }
        else {
        	return name.substring(index + 1).toLowerCase();
        }
    }
}

/**
 * 功能: 有向图
 */
class Graph{
	/**
	 * 功能: 图结点, 存储单个单词
	 */
	public static class Node{
		private String word;  //结点中存储的单词
		private HashMap<String, Pair<Node, Integer>> adjVertices; //邻接顶点及边的权值

		/**
		 * 功能: 构造函数
		 * 参数:要存储的单词, 邻接顶点及边的权值
		 */
		public Node(String word, HashMap<String, Pair<Node, Integer>> adjVertices) {
			this.word = word;
			this.adjVertices = adjVertices == null ? new HashMap<String, Pair<Node, Integer>>() : adjVertices;
		}

		/**
		 * 功能: 添加邻接顶点
		 * 参数: 图结点, 如果结点已经存在, 则传入已建结点, 否则新建
		 */
		public void addAdjVertex(Node addNode) {
			Pair<Node, Integer> adjVertex = adjVertices.get(addNode.word);
			if(adjVertex == null) {
				adjVertices.put(addNode.word, new Pair<Node, Integer>(addNode, 1));
			}
			else {
				adjVertex.setSecond(adjVertex.getSecond() + 1);
			}
		}

		/**
		 * 功能: 返回结点中存储的单词
		 */
		public String getWord() {
			return word;
		}

		/**
		 * 功能: 返回所有邻接顶点
		 */
		public HashMap<String, Pair<Node, Integer>> getAdjVertices(){
			return adjVertices;
		}
	}

	/**
	 * 功能: 构造函数
	 * 参数: 存储单词的数组, 相邻单词相邻存储
	 */
	public Graph(ArrayList<String> words) {
		if(words == null || words.isEmpty()) {
			return;
		}
		root = new Node(words.get(0), null);
		allNodes.put(words.get(0), root);
		Node preNode = root;
		for(int i=1; i < words.size(); ++i)
		{
			String newWord = words.get(i);
			Node node = allNodes.get(newWord), newNode = null;
			if(node == null)
			{
				newNode = new Node(newWord, null);
				allNodes.put(newWord, newNode);
				preNode.addAdjVertex(newNode);
				preNode = newNode;
			}
			else
			{
				preNode.addAdjVertex(node);
				preNode = node;
			}
		}
	}

	/**
	 * 功能: 命令行打印当前图结构
	 */
	public void printGraph() {
		for(String key: allNodes.keySet())
		{
			out.print(key + "\t=>\t");
			HashMap<String, Pair<Node, Integer>> adjVertices = allNodes.get(key).adjVertices;
			for(String word: adjVertices.keySet()){
				out.print(word + "\t" + adjVertices.get(word).getSecond() + "\t");
			}
			out.println();
		}
	}

	/**
	 * 功能: 返回图的所有结点
	 */
	public HashMap<String, Node> getAllNodes() {
		return allNodes;
	}

	/**
	 * 功能: 返回存储指定单词的结点
	 */
	public Node getNode(String word) {
		return allNodes.get(word);
	}

	private Node root = null;  //文本开始结点
	private HashMap<String, Node> allNodes = new HashMap<>();  //所有结点
}

public class PairProgramming {
    private static Graph graph = null;								//文本有向图
	private static ArrayList<String> text = null;					//存储文本所有单词
	private static ArrayList<String> words = null;					//所有出现的单词

	private static String startSource = null;						//源点
	private static Integer infinity = 0x3f3f3f3f;					//无穷大
	private static HashMap<String, String> prevWord = null;			//最短路径的前置结点
	private static HashMap<String, Integer> dist = new HashMap<>();	//源点到所有结点的最短距离
	private static String[] randomPath = null;						//随机游走路径

	/**
	 * 功能: 读取文件, 返回文本单词组成的数组
	 * 参数: 目标文件所在文件目录, 是否为读基准文本
	 */
	public static String[] readText(String catelogue, boolean basic){
		JFileChooser fc = new JFileChooser(catelogue);

		//设置文件选择参数
	    fc.setMultiSelectionEnabled(false);
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setFileHidingEnabled(true);
	    fc.setAcceptAllFileFilterUsed(false);
	    fc.setFileFilter(new MyFilter("txt"));

	    int returnValue = fc.showOpenDialog(null);
	    ArrayList<String> allWords = new ArrayList<>();
	    if (returnValue == JFileChooser.APPROVE_OPTION)
	    {
	        String path = fc.getSelectedFile().getPath();
	        try
	        {
	        	Scanner fin = new Scanner(Paths.get(path));
	        	while(fin.hasNext())
	        	{
	        		String line = fin.nextLine();
	        		line = line.replaceAll("[^(A-Za-z]"," ");  //除大小写字母外的所有字符替换为空格
                    line = line.toLowerCase();
	        		String[] lineWords = line.split(" +");	   //将句子分裂成单词, 一个或多个空格作为分隔符号
	        		allWords.addAll(Arrays.asList(lineWords));
	        	}
	        	fin.close();
	        }
	        catch (Exception e) {
	        	out.println(e.getMessage());
			}
	    }
	    if(basic) {
	    	text = allWords;
	    }
	    else {
	    	return allWords.toArray(new String[0]);
	    }
	    return new String[0];
	}

	/**
	 * 功能: 读取文件, 用户接口文件
	 * 参数: 目标文件所在文件目录
	 */
	public static void readText(String catelogue){
		readText(catelogue, true);
	}

	/**
	 * 功能: 生成有向图
	 */
	public static void generateGraph() {
		graph = new Graph(new ArrayList<String>(text));
		words = new ArrayList<>(graph.getAllNodes().keySet());
	}

	/**
	 * 功能: 返回文本有向图的信息
	 * 		 数组第一个元素为所有单词,用逗号分隔
	 * 		 剩余元素为所有的边,用顶点序号 + 边权值序号对表示
	 * 返回值: 数组表示, 图信息均转换为字符串存储
	 */
	public static String[] showDirectedGraph() {
		ArrayList<String> graphStr = new ArrayList<>();
		HashMap<String, Graph.Node> allNodes = graph.getAllNodes();
		graphStr.add(String.join(",", words));
		for(String key: allNodes.keySet())
		{
			HashMap<String, Pair<Graph.Node, Integer>> adjVertices = allNodes.get(key).getAdjVertices();
			for(String word: adjVertices.keySet())
			{
				String edge = key + "\t" + word + "\t" + adjVertices.get(word).getSecond().toString();
				graphStr.add(edge);
			}
		}
		return graphStr.toArray(new String[0]);
	}

	/**
	 * 功能: 查询桥接词
	 * 参数: 给定的两个单词
	 * 返回值: String(status@word1,word2,...) 字符串形式
	 * 	 status: 1.查询成功 2.没有桥接词 3.第一个单词不存在 4.第二个单词不存在
	 */
	public static String queryBridgeWords(String word1, String word2) {
	    word1 = word1.toLowerCase();
	    word2 = word2.toLowerCase();

		//第一个或第二个单词不存在
		if(words.indexOf(word1) == -1) {
			return "3,null";
		}
		if(words.indexOf(word2) == -1) {
			return "4,null";
		}

		//查询桥接词
		HashMap<String, Graph.Node> gnodes = graph.getAllNodes();
		HashMap<String, Pair<Graph.Node, Integer>> next1,next11;
		ArrayList<String> qwords = new ArrayList<>();
		next1 = gnodes.get(word1).getAdjVertices();
		for(String akey: next1.keySet())
		{
			next11 = gnodes.get(akey).getAdjVertices();
			for(String bkey : next11.keySet()) {
				if(bkey.equals(word2)) {
					qwords.add(akey);
				}
			}
		}

		//没有桥接词
		if(qwords.isEmpty()) {
			return "2,null";
		}

		//查询成功
		ArrayList<String> result = new ArrayList<>();
		result.add("1");
		result.addAll(qwords);
		return String.join(",", result);
	}

	/**
	 * 功能: 给定文本插入桥接词, 生成新文本, 然后写入文件
	 * 参数: 给定文本或者从文件读文本, inputText为空串则从文件读, 否则为前端读入文本
	 * 返回值: 生成的新文本, 无论是否从磁盘读文件, 均返回新生成的文本, 除去非法字符, 单词间空格分开
	 */
	public static String generateNewText(String inputText) {
		//处理输入文本或者读取文件
		String[] allWords = null;
		if(inputText.isEmpty()) {
			allWords = readText("C://Users/gzhang/Desktop/Lab1", false);
		}
		else {
			allWords = inputText.replaceAll("[^(A-Za-z]"," ").split(" +"); //分拆单词
		}

		//插入桥接词
		ArrayList<String> newWords = new ArrayList<>();
		for(int i=0; i < allWords.length-1; ++i)
		{
			newWords.add(allWords[i]);
			String[] queryResult = queryBridgeWords(allWords[i], allWords[i+1]).split(",");
			if(queryResult[0].equals("1")) {
				newWords.add(queryResult[1]);
			}
		}
		newWords.add(allWords[allWords.length-1]);

		//将结果写入文件并返回
		String newText = String.join(" ", newWords);
		try {
			PrintWriter writer = new PrintWriter("new_text.txt", "UTF-8");
			writer.println(newText);
			writer.close();
		}
		catch (Exception e) {
			out.println(e.getMessage());
		}
		return newText;
	}

	/**
	 * 功能: 二叉堆下沉操作
	 * 参数: 堆, 所有结点在堆中的位置, 下沉结点的位置, 堆的大小
	 */
	private static void pushDown(String[] heap, HashMap<String, Integer> position, Integer p, Integer size) {
		Integer cur = p.intValue();
		String word = heap[p];
		Integer distValue = dist.get(word);
		while (cur * 2 <= size)
		{
			Integer next = cur * 2;
			if (!next.equals(size) && dist.get(heap[next + 1]) < dist.get(heap[next])) {
				next++;
			}
			if (dist.get(heap[next]) < distValue)
			{
				heap[cur] = heap[next];
				position.replace(heap[cur], cur);
			}
			else {
				break;
			}
			cur = next;
		}
		heap[cur] = word;
		position.replace(heap[cur], cur);
	}

	/**
	 * 功能: 二叉堆上浮操作
	 * 参数: 堆, 所有结点在堆中的位置, 上浮结点的位置
	 */
	private static void pushUp(String[] heap, HashMap<String, Integer> position, Integer p){
		Integer cur = p.intValue();
		String word = heap[p];
		Integer distValue = dist.get(heap[p]);
		while(cur > 1)
		{
			Integer next = cur / 2;
			if(distValue < dist.get(heap[next]))
			{
				heap[cur] = heap[next];
				position.replace(heap[cur], cur);
			}
			else {
				break;
			}
			cur = next;
		}
		heap[cur] = word;
		position.replace(heap[cur], cur);
	}

	/**
	 * 功能: 初始化堆结构
	 * 参数: 结点个数
	 */
	private static Pair<String[], HashMap<String, Integer>> initHeap(Integer size){
		String[] heap = new String[size+1];
		HashMap<String, Integer> position = new HashMap<>();  //记录结点在堆中的位置

		//初始化数据结构
		for(String word: words) {
			dist.put(word, infinity);
		}
		dist.replace(startSource, 0);
		heap[1] = startSource;
		position.put(startSource, 1);
		for(Integer i=0, pos=2; !i.equals(size); ++i)
		{
			String word = words.get(i);
			if(word.equals(startSource)) {
				continue;
			}
			heap[pos++] = word;
			position.put(word, pos-1);
		}
		return new Pair<String[], HashMap<String, Integer>>(heap, position);
	}

	/**
	 * 功能: 计算单源最短路径, 并存储结果于preWord和dist中
	 * 参数: 起点单词
	 * 返回值: 状态标记 1.计算成功 2.起点单词不存在
	 */
	public static String calcAllShortestPath(String startWord) {
	    startWord = startWord.toLowerCase();

		//起点单词不存在
		if(words.indexOf(startWord) == -1) {
			return "2";
		}

		//计算成功,并存储
		Integer size = words.size();
		Integer heapSize = size;
		startSource = startWord;
		Pair<String[], HashMap<String, Integer>> initValue = initHeap(size);
		String[] heap = initValue.getFirst();
		HashMap<String, Integer> position = initValue.getSecond();

		prevWord = new HashMap<String, String>();
		while (heapSize > 0)
		{
			//贪心算法计算最短路径
			String start = heap[1];
			heap[1] = heap[heapSize--];
			position.replace(heap[1], 1);
			pushDown(heap, position, 1, heapSize);

			for(Pair<Graph.Node, Integer> node: graph.getNode(start).getAdjVertices().values())
			{
				String end = node.getFirst().getWord();
				if(dist.get(start) + node.getSecond() < dist.get(end))
				{
					prevWord.put(end, start);
					dist.replace(end, dist.get(start) + node.getSecond());
					pushUp(heap, position, position.get(end)); //更新结点在堆中的位置
				}
			}
		}
		return "1";
	}

	/**
	 * 功能: 查询指定的单源最短路径
	 * 参数: 终点单词
	 * 返回值: String(status@dist@word1,word2,...) 字符串形式
	 * 	status: 1.查询成功 2.不存在路径 3.终点单词不存在 用字符1-3表示
	 */
	public static String showOneShortestPath(String endWord) {
	    endWord = endWord.toLowerCase();

		//终点单词不存在
		if(words.indexOf(endWord) == -1) {
			return "3@0@null";
		}
		//不存在路径
		if(dist.get(endWord) == infinity) {
			return "2@0@null";
		}

		//查询成功
		ArrayList<String> path = new ArrayList<>();
		String cur = endWord;
		while(prevWord.get(cur) != null)
		{
			path.add(cur);
			cur = prevWord.get(cur);
		}
		path.add(cur);
		Collections.reverse(path); //正序路径
		return "1@" + dist.get(endWord) + "@" + String.join(",", path);
	}

	/**
	 * 功能: 计算两点之间的最短路径
	 * 参数: 起点单词, 结束单词
	 * 返回值: String(status@dist@word1,word2,...) 字符串形式
	 * 	 status: 1.查询成功 2.不存在路径 3.起点单词不存在 4.终点单词不存在 用字符1-4表示 5.word1==word2前端处理
	 */
	public static String calcShortestPath(String word1, String word2) {
	    word1 = word1.toLowerCase();
	    word2 = word2.toLowerCase();

		//起点单词不存在
		if(words.indexOf(word1) == -1) {
			return "3@0@null";
		}
		//终点单词不存在
		if(words.indexOf(word2) == -1) {
			return "4@0@null";
		}

		//初始化
		startSource = word1;
		String allShortestPath = null;

		//调用单源点最短路径算法, 生成新的有向图
		ArrayList<String> acceptedEdges = new ArrayList<>();
		calcAllShortestPath(word1);
		for(String start: words)
		{
			HashMap<String, Pair<Graph.Node, Integer>> adjVertices= graph.getNode(start).getAdjVertices();
			for(String end: adjVertices.keySet()) {
				if(dist.get(end) - dist.get(start) == adjVertices.get(end).getSecond()) {
					acceptedEdges.add(start + "," +end);
				}
			}
		}

		//计算两点之间的所有路径, 即所有最短路径
		ArrayList<String> visitedEdges = new ArrayList<>();
		ArrayList<String> path = new ArrayList<>();
		path.add(word1);
		while(true)
		{
			//DFS找出一条路径
			Boolean flag = true; //标记当前路径是否还能往前
			L:while (flag)
			{
				flag = false;
				String curWord = path.get(path.size() - 1);
				if (curWord.equals(word2)) {
					break L;
				}
				for (Pair<Graph.Node, Integer> node : graph.getNode(curWord).getAdjVertices().values()) {
					String end = node.getFirst().getWord();
					String edge = curWord + "," + end;
					if (acceptedEdges.indexOf(edge) != -1 && visitedEdges.indexOf(edge) == -1)
					{
						path.add(end);
						flag = true;
						break;
					}
				}
			}

			//收集路径
			int length = path.size();
			if(length > 1)
			{
				if (path.get(length-1).equals(word2)) {
					allShortestPath += String.join(",", path) + ";";
				}

				//弹出栈顶结点
				visitedEdges.add(path.get(length-2) + "," + path.get(length-1));
				path.remove(length-1);
			}
			else {
				break;
			}
		}

		//不存在路径
		if(allShortestPath == null) {
			return "2@0@null";
		}
		return "1@" + dist.get(word2) + "@" + allShortestPath.substring(4, allShortestPath.length()-1);
	}

	/**
	 * 功能: 随机游走
	 * 参数: 起点单词
	 * 返回值: String(status@word,word1,word2,...) 字符串形式
     *  status：1.结尾单词无邻接顶点 2.遇到重复边 3.起点单词不存在 字符1-3表示
	 */
	public static String randomWalk(String startWord) {
	    startWord = startWord.toLowerCase();

		Graph.Node startNode = graph.getNode(startWord);
		//起点单词不存在
		if(startNode == null)
		{
			randomPath = new String[]{"ERROR"};
			return "3@null";
		}

		//随机游走
		HashMap<String, Pair<Graph.Node, Integer>> adjVertices = startNode.getAdjVertices();
		ArrayList<String> traverseWords = new ArrayList<>();
		traverseWords.add(startWord);
		String preWord = startWord;
		LOOP: while(!adjVertices.isEmpty())
		{
			String[] adjWords = adjVertices.keySet().toArray(new String[0]);
			Random rand = new Random(System.currentTimeMillis());
			String nextWord = adjWords[rand.nextInt(adjVertices.keySet().size())];
			traverseWords.add(nextWord);
			for(int i=1; i < traverseWords.size() - 1; ++i)	{ //检测边是否已经走过
				if(traverseWords.get(i-1).equals(preWord) && traverseWords.get(i).equals(nextWord)) {
					break LOOP;
				}
			}
			preWord = nextWord;
			adjVertices = adjVertices.get(nextWord).getFirst().getAdjVertices();
		}
		randomPath = traverseWords.toArray(new String[0]);
		String randomPath = String.join(",", traverseWords);
		if(adjVertices.isEmpty()) {
            return "1@" + randomPath;
		}
		else {
		    return "2@" + randomPath;
		}
	}

	/**
	 * 功能: 将随机游走的路径写入文本
	 * 参数: 随机游走的步数
     * 返回值: 文件写入状态
	 */
	public static String writeRandomPath(String pos){
		Integer length = Integer.parseInt(pos);
		try {
			PrintWriter writer = new PrintWriter("random_path.txt");
			if(length >= randomPath.length)
			{
				writer.println(String.join(", ", randomPath));
				writer.close();
				return "1";
			}
			else
			{
				writer.println(String.join(", ", Arrays.copyOf(randomPath, length + 1)));
				writer.close();
				return "1";
			}
		}
		catch (Exception e){
			out.println(e.getMessage());
			return "0";
		}
	}

	/**
	 *功能: 测试程序, 此测试程序仅作为编程使用
	 */
	public static void main(String[] args) {
		readText("C://Users/gzhang/Desktop/Lab1", true);
		generateGraph();
		graph.printGraph();

		//生成新文本
		out.println();
		out.println(generateNewText(""));

		//单源最短路径
		out.println();
		if(calcAllShortestPath("new").equals("1")) {
			for(String word: words) {
			 	out.println(showOneShortestPath(word));
			}
		}

		//两点最短路径
		out.println();
		out.println(calcShortestPath("new", "and"));

		//随机游走
		out.println();
		Scanner in = new Scanner(System.in);
		do {
			out.print("请输入起点单词:");
			String word = in.nextLine();
			if(word.equals("exit")) {
				break;
			}
			out.println(randomWalk(word));
			out.print("请输入步数:");
			Integer num = Integer.parseInt(in.nextLine());
			writeRandomPath(num.toString());
		} while (true);
		in.close();
	}
}