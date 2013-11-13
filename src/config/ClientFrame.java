package config;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;



import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import config.mockDB.ConfigModel;
import config.socket.ClientSocket;

public class ClientFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField jtf = new JTextField("127.0.0.1");
	private JTextArea jta = new JTextArea();
	private JButton button = new JButton("连接");
	private JButton submitButton = new JButton("更新至服务器");
	private JButton addButton = new JButton("新加组");
	private JPanel panelAbove = null;
	
	private JTable table = new JTable();
	private JScrollPane jsp = new JScrollPane(table);
	
	private DefaultMutableTreeNode  rootNode = new DefaultMutableTreeNode("DTS");
	private DefaultTreeModel modelTree = new DefaultTreeModel(rootNode, true);
	private JTree tree = new JTree(modelTree);
	private DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
	private JScrollPane treeView = new JScrollPane(tree);
	
	private ClientSocket client = null;
	private String command = null;

	private String current_directory = null;
	private String path= null;
	private String DTS_CPP = null;
	private String DTS_JAVA = null;
	private String DTS_GCC = null;
	private boolean isConnected = true;
	private boolean isOpened = false;
		
	public static void main(String[] args) {
		new ClientFrame("F:\\Workspaces\\eclipse\\Test");
		//new ClientFrame(args[0]);
	}

	public ClientFrame(String filepath){
		path = filepath;
		DTS_CPP = path + "\\DTSCPP\\set\\config.mdb";
		DTS_JAVA = path + "\\DTSJava\\set\\config.mdb";
		DTS_GCC = path + "\\DTSGCC\\set\\config.mdb";
		
		panelAbove = new JPanel();
		panelAbove.setLayout(new BorderLayout());
		panelAbove.add(new JLabel("Server IP:"), BorderLayout.WEST);
		panelAbove.add(jtf, BorderLayout.CENTER);
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new BorderLayout());
		newPanel.add(button,BorderLayout.WEST);
		newPanel.add(submitButton,BorderLayout.CENTER);
		newPanel.add(addButton, BorderLayout.EAST);
		panelAbove.add(newPanel,BorderLayout.EAST);
		jtf.setHorizontalAlignment(JTextField.LEFT);
		getContentPane().setLayout(new BorderLayout());
		
//		JScrollPane notify_text = new JScrollPane(jta);
//		notify_text.setSize(600, 5);
//		JSplitPane notify_panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jsp, notify_text);
//		notify_panel.setOneTouchExpandable(true);		
		
		JSplitPane innerpanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, jsp);
		
		//当导出为jar包时,使用如下方式
//		java.net.URL imgURL = getClass().getResource("/res/mdb.gif");
//		render.setLeafIcon(new ImageIcon(imgURL));
		render.setLeafIcon(new ImageIcon("res/mdb.gif"));
		tree.setCellRenderer(render);
		
		tree.setShowsRootHandles(true);
		innerpanel.setContinuousLayout(true);
		//innerpanel.setOneTouchExpandable(true);
		innerpanel.setDividerLocation(1.0);
		JSplitPane outerpanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelAbove, innerpanel);
		add(outerpanel, BorderLayout.CENTER);
		
		jtf.addActionListener(this);
		setTitle("DTS ScanSet");
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		button.addActionListener(this);
		submitButton.addActionListener(this);
		addButton.addActionListener(this);
	}
	
	private void showConfig(String filepath){
		isOpened = true;
		ConfigModel mode = new ConfigModel(filepath);
		table.setModel(mode);
		for(int i=0; i<table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(40);
		table.setRowHeight(20);
		table.setPreferredScrollableViewportSize(new Dimension(150, 400));
		jsp.setAutoscrolls(true);
	}
		
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button){
			String ipAddress = jtf.getText();
			String reg = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$";
			ipAddress = ipAddress.trim();
			if( !ipAddress.matches(reg)){
				jta.append("Please fill the path\n");
				return ;
			}
			//client = new ClientSocket("10.108.161.103");
			client = new ClientSocket(ipAddress);
			File[][] files = client.getFilesList();
			if(files != null ){
				jsp.remove(table);
				isConnected = true;
				isOpened = false;
			}
			createNodes(rootNode, files);
		}else if(e.getSource() == submitButton){
			if(isConnected && isOpened){
				client.saveConfig(command, current_directory);
				JOptionPane.showConfirmDialog(null,"已上传至服务器","提示" ,  JOptionPane.CLOSED_OPTION);
			}else
				JOptionPane.showConfirmDialog(null,"未连接服务器,或者先双击组名打开数据库","提示" ,  JOptionPane.CLOSED_OPTION);
		}else if(e.getSource() == addButton){
			if(isConnected)
				addGroup();
			else
				JOptionPane.showConfirmDialog(null,"未连接服务器","提示" ,  JOptionPane.CLOSED_OPTION);
		}
	}
	
	private void createNodes(DefaultMutableTreeNode top, File filelist[][]) {
		int file_num=0;
		rootNode.removeAllChildren();
		for(int i=0; i< filelist.length; i++){
			if(filelist[i].length == 0){
				file_num++;
				continue;
			}
			File tempFile = filelist[i][0].getParentFile();
			String s = tempFile.getName();
			DefaultMutableTreeNode versionNode = new DefaultMutableTreeNode(s);
			for(File file : filelist[i]){
				DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(file.getName(),false);
				versionNode.add(groupNode);
			}
			top.add(versionNode);
		}
		if(file_num == 3){
			JOptionPane.showConfirmDialog(null,"DTSCPP, DTSGCC, DTSJava下都没有配置文件, 新建一个组试试","提示",  JOptionPane.CLOSED_OPTION);
		}
		modelTree.reload();
		tree.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(tempNode.isLeaf() && me.getClickCount() == 2){
					String s = tempNode.getParent().toString();
					command =s + "/" + tempNode.toString();
					if(s.equals("DTSGCC")){
						current_directory = DTS_GCC;
					}
					else if(s.equals("DTSCPP")){
						current_directory = DTS_CPP;
					}
					else if(s.equals("DTSJava")){
						current_directory = DTS_JAVA;
					}
					client.getConfig(command, current_directory);
					System.out.println("正在从服务器下载文件\n"); 
					showConfig(current_directory);
					System.out.println("打开数据库\n");
				}
			}
		});
	}
	
	private void addGroup(){
		JPanel panel1 = new JPanel();
		JLabel versionName = new JLabel("版本:");
		String[] v = {"DTSCPP", "DTSGCC", "DTSJava"};
		final JComboBox version = new JComboBox(v);
		panel1.setLayout(new BorderLayout());
		panel1.add(versionName, BorderLayout.WEST);
		panel1.add(version, BorderLayout.EAST);
		panel1.setSize(200, 30);
		JPanel panel2 = new JPanel();
		JLabel groupName = new JLabel("组名:");
		final JTextArea jtf = new JTextArea();
		panel2.setLayout(new BorderLayout());
		panel2.add(groupName, BorderLayout.WEST);
		panel2.add(jtf, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(panel1, BorderLayout.WEST);
		panel.add(panel2, BorderLayout.CENTER);
		final JFrame jf = new JFrame();
		jf.setLayout(new BorderLayout());
		jf.add(panel, BorderLayout.NORTH);
		JButton subButton = new JButton("提交");
		subButton.setSize(30, 20);
		
		jf.add(subButton, BorderLayout.CENTER);
		jf.setSize(600, 100);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		subButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String group_name = jtf.getText();
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				String selectNodeName = selectNode.getUserObject().toString();
				if(selectNodeName.equals("DTS") || selectNode.isLeaf()){
					JOptionPane.showConfirmDialog(null,"所选目录不能创建组,请在版本下建组","提示" ,  JOptionPane.CLOSED_OPTION);
					jf.dispose();
					return;
				}
				boolean flag = client.AddGroup(group_name);
				if( flag ){
					//int response = JOptionPane.showConfirmDialog(null,"新增组成功","提示" ,  JOptionPane.CLOSED_OPTION); 
					DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(group_name, false);
					modelTree.insertNodeInto(newChild, selectNode, selectNode.getChildCount());
					TreeNode[] nodes = modelTree.getPathToRoot(newChild);
					TreePath path = new TreePath(newChild);
					tree.scrollPathToVisible(path);
				}
				jf.dispose();
				/*
				String group_name = jtf.getText();
				if(group_name == null || group_name.isEmpty() || group_name.contains("\\") || group_name.contains("/")){
					JOptionPane.showConfirmDialog(null,"组名不能为空，不要在名字中出现'\\'或者'/'","提示" ,  JOptionPane.CLOSED_OPTION);
					return ;
				}
				boolean flag = client.AddGroup(group_name);
				if( flag ){
					int response = JOptionPane.showConfirmDialog(null,"新增组成功","提示" ,  JOptionPane.CLOSED_OPTION); 
					
					File[][] files = client.getFilesList();
					createNodes(rootNode, files);
					if(response ==0)
						jf.dispose();
				}else{
					JOptionPane.showConfirmDialog(null, "新增组失败，\n检查是否该组名已经存在","提示",  JOptionPane.CLOSED_OPTION);
					return;
				}
				*/
			}
		});
	}
	
}
