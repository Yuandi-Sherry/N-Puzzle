package solution;

import jigsaw.Jigsaw;
import jigsaw.JigsawNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;

/**
 * 在此类中填充算法，完成重拼图游戏（N-数码问题）
 */
public class Solution extends Jigsaw {
	// queue BFS
	Queue<JigsawNode> open = new LinkedList<JigsawNode>();
	HashSet<JigsawNode> close =new HashSet<JigsawNode> ();

	/**
	 * 拼图构造函数
	 */
	public Solution() {

	}

	/**
	 * 拼图构造函数
	 * @param bNode - 初始状态节点
	 * @param eNode - 目标状态节点
	 */
	public Solution(JigsawNode bNode, JigsawNode eNode) {
		super(bNode, eNode);
		
	}

	/**
	 *（实验一）广度优先搜索算法，求指定5*5拼图（24-数码问题）的最优解
     * 填充此函数，可在Solution类中添加其他函数，属性
	 * @param bNode - 初始状态节点
     * @param eNode - 目标状态节点
	 * @return 搜索成功时为true,失败为false
	 */
	public boolean BFSearch(JigsawNode bNode, JigsawNode eNode) {

		beginJNode = bNode;
		endJNode = eNode;
		open.offer(bNode);

		while(open.size() != 0) {

			// this.searchedNodesNum++;
			// 访问open列表中的第一个节点v, 删除节点v
			currentJNode = open.poll();
			
			// 若v为目标节点，则搜索成功，退出
			if(currentJNode.equals(endJNode)) {
				this.getPath();
				break;
			}

			// 节点v，放入close列表中
			close.add(currentJNode);

			int [] canMove = currentJNode.canMove();
			for(int i = 0; i < 4; i++) {
				//System.out.println("canMove[i] " + canMove[i] );
			}



			////System.out.println("currentJNode.toMatrixString()");
			////System.out.println(currentJNode.toMatrixString());
			for(int i = 0; i < 4; i++) {
				if(canMove[i] == 1) {
					//System.out.println("canMove[i] == 1");
					JigsawNode temp = new JigsawNode(currentJNode);
					// //System.out.println("i = "+ i);
					//System.out.println("temp.toMatrixString()" + temp.toMatrixString());
					

					switch(i) {
						case 0:
						temp.moveEmptyUp();
						break;
						case 1:
						temp.moveEmptyDown();
						break;
						case 2:
						temp.moveEmptyLeft();
						break;
						case 3:
						temp.moveEmptyRight();
						break;
					}
					//System.out.println("temp.toMatrixString()" + temp.toMatrixString());
					////System.out.println("currentJNode.hashCode(): "+ currentJNode.hashCode());
					if(!close.contains(temp)) {
						//System.out.println("open Add: "+ i);
						open.offer(temp);
						
					}
				}
			}
			////System.out.println("Depth of the current node is:" + this.getCurrentJNode().getNodeDepth());
		}
		

		System.out.println("Jigsaw BFS Search Result:");
        System.out.println("Begin state:\n" + this.getBeginJNode().toMatrixString());
        System.out.println("End state:\n" + this.getEndJNode().toMatrixString());
        System.out.println("Solution Path: ");
        System.out.println(this.getSolutionPath());
        //System.out.println("Total number of searched nodes:" + this.getSearchedNodesNum());
        System.out.println("Depth of the current node is:" + this.getCurrentJNode().getNodeDepth());
		return this.isCompleted();
	}


	/**
	 *（Demo+实验二）计算并修改状态节点jNode的代价估计值:f(n)
	 * 如 f(n) = s(n). s(n)代表后续节点不正确的数码个数
     * 此函数会改变该节点的estimatedValue属性值
     * 修改此函数，可在Solution类中添加其他函数，属性
	 * @param jNode - 要计算代价估计值的节点
	 */
	public void estimateValue(JigsawNode jNode) {
		//System.out.println("Solution Path: ");
		int s = 0; // 后续节点不正确的数码个数
		int dimension = JigsawNode.getDimension();
		int wrongNum = 0; // 所有 放错位的数码 个数
		int sumDistance = 0;
		int [] curState = new int[JigsawNode.getDimension()*JigsawNode.getDimension()];
		int [] tarState = new int[JigsawNode.getDimension()*JigsawNode.getDimension()];

		for (int index = 1; index <= dimension * dimension; index++) {
			if(index != dimension * dimension) {
				if (jNode.getNodesState()[index] + 1 != jNode.getNodesState()[index + 1]) {
					s++;
				}
			}
			
			if (jNode.getNodesState()[index] != endJNode.getNodesState()[index]) {
				wrongNum++;
			}
			curState[jNode.getNodesState()[index]] = index - 1;
			tarState[endJNode.getNodesState()[index]] = index - 1;

		}

		for(int i = 1; i < JigsawNode.getDimension()*JigsawNode.getDimension(); i++) {
			sumDistance += Math.abs(curState[i]/dimension - tarState[i]/dimension) 
				+ Math.abs(curState[i]%dimension - tarState[i]%dimension);
		}
		int value = 3*sumDistance + s + wrongNum;

		int dE = value - jNode.getParent().getEstimatedValue(); 

		if ( dE <= 0 ) //表达移动后得到更优解，则总是接受移动
			value *= Math.random();
		jNode.setEstimatedValue(value);
	}
}
