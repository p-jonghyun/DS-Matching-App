package Viewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.*;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Team.Role;
import Team.Team;
import User.User;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TeamsView extends Viewer implements MouseListener {
   private static final long serialVersionUID = 1L;
   
   private JPanel contentPane;
   private JTable table;
   private String team_id = null;
   private JList<String> list;

   private ClientApp client;
   
   JButton btn_reg;     
   private List<Team> teamList; 
   private String selectedTeamName;
   
   public TeamsView(ClientApp client, String team_id, List<Team> teamList) {
      super(client);
      
      this.client = client;
      this.teamList = teamList;
      this.team_id = team_id;		//user가 팀에 가입되있지 않은 상태면 null임을 가정(확인 필요)
      if(this.team_id.equals("0")) this.team_id = null;
      init();
      
      // TODO Auto-generated constructor stub
   }
   
   public void init() {       

      contentPane = this;
      contentPane.setBackground(new Color(248, 248, 255));
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane.setLayout(null);
      
      
      JPanel panel_team = new JPanel();
      panel_team.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      panel_team.setBounds(30, 70, UIConst.WIDTH-60, 180);
      contentPane.add(panel_team);
      panel_team.setLayout(null);

      /***속한 팀이름 또는 팀에 가입해달라는 라벨****/
      JLabel lb_team = new JLabel("(team name)");
      lb_team.setFont(new Font("굴림", Font.PLAIN, 16));
      lb_team.setBounds(125, 20, 212, 41);
      if(team_id!=null)
         lb_team.setText("팀 이름: "+team_id);
      else
         lb_team.setText("팀에 가입해주세요");
      panel_team.add(lb_team);
      
      /***팀 입장 또는 팀 생성 버튼****/
      JButton btn_team = new JButton("팀 생성");
      btn_team.setFont(new Font("굴림", Font.PLAIN, 20));
      if(team_id!=null)
         btn_team.setText("팀 입장");
      btn_team.addActionListener(this);
      btn_team.setBounds(186, 71, 151, 33);
      panel_team.add(btn_team);
      
      /***프로필 버튼****/
      JButton btn_profile = new JButton("프로필 보기");
      btn_profile.setFont(new Font("굴림", Font.PLAIN, 20));
      btn_profile.setBounds(186, 114, 151, 33);
      btn_profile.addActionListener(this);
      panel_team.add(btn_profile);
      
      /***프로필 이미지****/
      //일단 라벨만 붙혀놈
      JLabel lb_image = new JLabel("profile image");
      lb_image.setIcon(null);
      lb_image.setBounds(30, 20, 101, 133);
      panel_team.add(lb_image);
      
      /***팀 목록*****/
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(30, 260, UIConst.WIDTH-60, 244);
      contentPane.add(scrollPane);

      list = new JList<String>();
      updateTeamList();
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      list.addMouseListener(this);
      scrollPane.setViewportView(list);
      
      JLabel lb_teamList = new JLabel("팀 목록");
      lb_teamList.setHorizontalAlignment(SwingConstants.CENTER);
      scrollPane.setColumnHeaderView(lb_teamList);
      lb_teamList.setFont(new Font("굴림", Font.PLAIN, 20));
      
      
      /****리스트에서 선택한 팀에 가입신청 버튼*****/
      btn_reg = new JButton("팀 가입");
      btn_reg.setFont(new Font("굴림", Font.PLAIN, 16));
      btn_reg.addActionListener(this);
      btn_reg.setBounds(30, 560, UIConst.WIDTH-60, 75);
      btn_reg.setVisible(false);
      contentPane.add(btn_reg);
      
      /****리스트에서 선택한 팀의 구성 표****/
      setOrganizationTable();
      table.setVisible(true);
      contentPane.add(table);
      
      
      
      setSize(400,600);
      
   }
   
   
   private void setOrganizationTable() {
      table = new JTable();
      table.setModel(new DefaultTableModel(
         new Object[][] {
            {null, "current", "maximum"},
            {"designer", null, null},
            {"developer", null, null},
            {"planner", null, null},
         },
         new String[] {
            " ", "current", "maximum"
         }
      ) {
         Class[] columnTypes = new Class[] {
            String.class, String.class, String.class
         };
         public Class getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
         }
         boolean[] columnEditables = new boolean[] {
            false, false, false
         };
         public boolean isCellEditable(int row, int column) {
            return columnEditables[column];
         }
      });
      table.getColumnModel().getColumn(0).setResizable(false);
      table.getColumnModel().getColumn(1).setResizable(false);
      table.getColumnModel().getColumn(2).setResizable(false);
      table.setCellSelectionEnabled(true);
      table.setColumnSelectionAllowed(true);
      table.setBounds(30, 520, UIConst.WIDTH-60, 150);
      
   }

   
   private void updateTeamList() {
         
      DefaultListModel<String> listModel = new DefaultListModel<String>();
      
      for(int i=0; i<teamList.size(); i++) {
    	  listModel.addElement(teamList.get(i).getName());
      }
      list.setModel(listModel);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      JButton b = (JButton) e.getSource();

      switch(b.getText()) {
         case "팀 생성":
            client.ChangeView(new TeamCreateView(client));
            break;
         case "팀 입장":
            client.requestMyTeam(String.valueOf(this.team_id));
            break;
         case "프로필 보기":	//id->email
            client.requestGetUser(client.user_id);
            break;
         case "팀 가입":
            client.applyTeam(selectedTeamName);
            client.print("팀 가입 신청");
	            
            break;


      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      Map<Role, Integer> currentRole = teamList.get(list.getSelectedIndex()).getCurrentRoles();
      Map<Role, Integer> maximumRole = teamList.get(list.getSelectedIndex()).getRoleLimits();		//Team class에 getRoleLimits() 추가 필요
      selectedTeamName = teamList.get(list.getSelectedIndex()).getName();
      /***팀 구성표 업데이트***/
      Role[] roles = Role.values();
      int cur_designer = 0, cur_developer = 0, cur_planner = 0;
      int max_designer = 0, max_developer = 0, max_planner = 0;

      for(int i=0; i<9; i++) {
         if(currentRole.get(roles[i]) == null)
            currentRole.put(roles[i], 0);
      }

      for(int i=0; i<9; i++) {
         if(maximumRole.get(roles[i]) == null)
            maximumRole.put(roles[i], 0);
      }

      for(int i=0; i<=3; i++) {
         if(i<=2) {
            Role trole = roles[i];
            int cnt = currentRole.get(trole);
            cur_designer += currentRole.get(roles[i]);      max_designer += maximumRole.get(roles[i]);
         }
         cur_developer += currentRole.get(roles[i+3]);      max_designer += maximumRole.get(roles[i+3]);
         if(i<=1) {
            cur_planner += currentRole.get(roles[i+7]);      max_planner += maximumRole.get(roles[i+7]);
         }
      }
      //([개발자/기획자/디자인], [현재 인원], [모집 인원])
      table.setValueAt(cur_designer, 1, 1);      table.setValueAt(max_designer, 1, 2);
      table.setValueAt(cur_developer, 2, 1);      table.setValueAt(max_developer, 2, 2);
      table.setValueAt(cur_planner, 3, 1);      table.setValueAt(max_planner, 3, 2);

      table.setVisible(true);
      if(team_id==null)      //소속 팀이 없으면 가입 버튼 보이기
         btn_reg.setVisible(true);
   }

   @Override
   public void mousePressed(MouseEvent e) {

   }

   @Override
   public void mouseReleased(MouseEvent e) {

   }

   @Override
   public void mouseEntered(MouseEvent e) {

   }

   @Override
   public void mouseExited(MouseEvent e) {

   }
}
