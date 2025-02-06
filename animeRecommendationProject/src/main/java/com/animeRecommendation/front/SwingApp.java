package com.animeRecommendation.front;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.animeRecommendation.model.Anime;
import com.animeRecommendation.model.ListAnime;
import com.animeRecommendation.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SwingApp extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField searchGenres;
    private JPanel resultsPanel;
    private Long userId;
    private JButton logInButton;
    private JButton registrationButton;
    private JButton logOutButton;

    public SwingApp() {
    	setTitle("Anime Search App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 986, 816);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 128, 255));
        contentPane.setForeground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        searchGenres = new JTextField();
        searchGenres.setBounds(212, 78, 610, 32);
        contentPane.add(searchGenres);
        searchGenres.setColumns(10);
        
        JLabel message = new JLabel("Put genres");
        message.setForeground(new Color(0, 0, 0));
        message.setFont(new Font("Stencil", Font.PLAIN, 24));
        message.setBackground(new Color(240, 240, 240));
        message.setBounds(362, 34, 246, 33);
        message.setHorizontalAlignment(JLabel.CENTER);
        message.setVerticalAlignment(JLabel.CENTER);
        contentPane.add(message);

        JButton buttonSearch = new JButton("Search");
        buttonSearch.setBackground(new Color(0, 64, 128));
        buttonSearch.setForeground(new Color(255, 255, 255));
        buttonSearch.addActionListener(e -> {
        	String genres = searchGenres.getText().trim();
            if (!genres.isEmpty()) {
                searchAnimes(genres);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter at least one genre", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonSearch.setBounds(832, 77, 114, 34);
        buttonSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(buttonSearch);
        
        JComboBox filters = new JComboBox();
        filters.setBackground(new Color(0, 64, 128));
        filters.setForeground(new Color(255, 255, 255));
        filters.setBounds(30, 78, 73, 33);
        filters.addItem("Year");
        filters.addItem("Ranking");
        filters.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(filters);

        resultsPanel = new JPanel();
        resultsPanel.setForeground(new Color(255, 255, 255));
        resultsPanel.setLayout(new GridLayout(0, 2, 10, 10)); 
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBounds(30, 120, 915, 650); 
        contentPane.add(scrollPane);

        JButton viewList = new JButton("List");
        viewList.setForeground(new Color(255, 255, 255));
        viewList.setBackground(new Color(0, 64, 128));
        viewList.addActionListener(e -> {
            if (userId == null) {
                JOptionPane.showMessageDialog(null, "Please, sign in before to see your anime list.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                getUserAnimeList(userId);
            }
        });
        viewList.setBounds(30, 21, 114, 34);
        viewList.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(viewList);
         
        JButton filterButton = new JButton("Filter");
        filterButton.setBackground(new Color(0, 64, 128));
        filterButton.setForeground(new Color(255, 255, 255));
        filterButton.addActionListener(e -> {
            String selectedFilter = (String) filters.getSelectedItem();
            orderAnimes(selectedFilter);
        });
        filterButton.setBounds(113, 78, 89, 33);
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(filterButton);
        
        registrationButton = new JButton("Sign in");
        registrationButton.setBackground(new Color(0, 64, 128));
        registrationButton.setForeground(new Color(255, 255, 255));
        registrationButton.addActionListener(e -> openRegisterForm());
        registrationButton.setBounds(790, 21, 73, 34);
        registrationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(registrationButton);
        
        logInButton = new JButton("Log in");
        logInButton.setBackground(new Color(0, 64, 128));
        logInButton.setForeground(new Color(255, 255, 255));
        logInButton.addActionListener(e -> openLoginForm());
        logInButton.setBounds(873, 21, 73, 34);
        logInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(logInButton);
        
        logOutButton = new JButton("Log out");
        logOutButton.setBackground(new Color(255, 64, 64));
        logOutButton.setForeground(new Color(255, 255, 255));
        logOutButton.setBounds(790, 21, 156, 34);
        logOutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logOutButton.addActionListener(e -> logOut());
        logOutButton.setVisible(false);  
        contentPane.add(logOutButton);
    }

    private void openLoginForm() {
        String name = JOptionPane.showInputDialog(null, "Enter your name:", "Login", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            Long id = getUserIdByName(name.trim());
            if (id != null) {
                userId = id;
                JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateUIOnLogin();
            } else {
                JOptionPane.showMessageDialog(null, "User not found. Please register.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openRegisterForm() {
        String name = JOptionPane.showInputDialog(null, "Enter your name:", "Register", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            registerUser(name.trim());
        } else {
            JOptionPane.showMessageDialog(null, "The name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchAnimes(String genres) {
        try {
            String urlString = "http://localhost:8080/animes/search?genres=" + URLEncoder.encode(genres, "UTF-8");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                JOptionPane.showMessageDialog(null, "Failed to fetch animes. Status: " + conn.getResponseCode(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            parseAndDisplayAnimes(response.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching animes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void registerUser(String name) {
        try {
            String urlString = "http://localhost:8080/users";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"name\":\"" + name + "\"}";
            conn.getOutputStream().write(jsonInputString.getBytes("UTF-8"));

            if (conn.getResponseCode() == 201) {
                JOptionPane.showMessageDialog(null, "User successfully registered!", "Success", JOptionPane.INFORMATION_MESSAGE);
                userId = getUserIdByName(name);
                updateUIOnLogin();
            } else {
                JOptionPane.showMessageDialog(null, "Error registering user", "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logOut() {
        userId = null;
        JOptionPane.showMessageDialog(null, "Logged out successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);
        updateUIOnLogout();
    }

    private void updateUIOnLogin() {
        logInButton.setVisible(false);
        registrationButton.setVisible(false);
        logOutButton.setVisible(true);
    }

    private void updateUIOnLogout() {
        logInButton.setVisible(true);
        registrationButton.setVisible(true);
        logOutButton.setVisible(false);
    }
    
    private Long getUserIdByName(String name) {
        try {
            String urlString = "http://localhost:8080/users/login/" + URLEncoder.encode(name, "UTF-8");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(response.toString(), User.class);
            return user.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    
    private void getUserAnimeList(Long userId) {
        try {
            String urlString = "http://localhost:8080/userListAnimes/" + userId;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                JOptionPane.showMessageDialog(null, "Failed to fetch user anime list. Status: " + conn.getResponseCode(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            parseAndDisplayUserAnimeList(response.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching user anime list", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void orderAnimes(String criterio) {
        Component[] components = resultsPanel.getComponents();
        List<JPanel> animeCards = new ArrayList<>();

        for (Component comp : components) {
            if (comp instanceof JPanel) {
                animeCards.add((JPanel) comp);
            }
        }

        animeCards.sort((panel1, panel2) -> {
            JLabel label1 = (JLabel) panel1.getComponent(0);
            JLabel label2 = (JLabel) panel2.getComponent(0);

            Anime anime1 = searchAnimeByName(label1.getText());
            Anime anime2 = searchAnimeByName(label2.getText());

            if (anime1 == null || anime2 == null) {
                return 0;
            }

            if ("Year".equals(criterio)) {
                return Integer.compare(anime2.getYear(), anime1.getYear()); 
            } else if ("Ranking".equals(criterio)) {
                return Double.compare(anime2.getRanking(), anime1.getRanking()); 
            }

            return 0;
        });

        resultsPanel.removeAll();

        for (JPanel panel : animeCards) {
            resultsPanel.add(panel);
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    private Anime searchAnimeByName(String nombre) {
        try {
            String urlString = "http://localhost:8080/animes?name=" + URLEncoder.encode(nombre, "UTF-8");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Anime> animes = objectMapper.readValue(response.toString(), new TypeReference<List<Anime>>() {});

            return animes.isEmpty() ? null : animes.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private void addToList(Anime anime) {
        if (userId == null) {
            JOptionPane.showMessageDialog(null, "Please register before adding animes to your list.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String urlString = "http://localhost:8080/userListAnimes?userId=" + userId + "&animeId=" + anime.getId();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 201) {
                JOptionPane.showMessageDialog(null, anime.getName() + " added to your list!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error adding anime", "Error", JOptionPane.ERROR_MESSAGE);
            }

            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding anime", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parseAndDisplayAnimes(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Anime> animes = objectMapper.readValue(jsonResponse, new TypeReference<List<Anime>>() {});

            resultsPanel.removeAll(); 

            for (Anime anime : animes) {
                JPanel animeCard = createAnimeCard(anime);
                resultsPanel.add(animeCard);
            }

            resultsPanel.revalidate();
            resultsPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error processing results", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createAnimeCard(Anime anime) {
        JPanel animeCard = new JPanel();
        animeCard.setLayout(new GridBagLayout());
        animeCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        animeCard.setPreferredSize(new Dimension(200, 250));

        Color[] colors = {Color.CYAN, Color.PINK, Color.ORANGE, Color.GREEN, Color.LIGHT_GRAY};
        animeCard.setBackground(colors[new Random().nextInt(colors.length)]);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        JLabel nameLabel = new JLabel(anime.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        animeCard.add(nameLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JTextArea description = new JTextArea(anime.getDescription(), 3, 20); 
        description.setFont(new Font("Arial", Font.BOLD, 15));
        description.setForeground(Color.WHITE);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setOpaque(false);
        description.setEditable(false);
        description.setFocusable(false);
        description.setBackground(new Color(0, 0, 0, 0));
        description.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 

        animeCard.add(description, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 64, 128));
        addButton.setForeground(new Color(255, 255, 255));
        addButton.addActionListener(e -> addToList(anime));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        animeCard.add(addButton, gbc);

        return animeCard;
    }

    private void parseAndDisplayUserAnimeList(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ListAnime> animeList = objectMapper.readValue(jsonResponse, new TypeReference<List<ListAnime>>() {});

            resultsPanel.removeAll();

            for (ListAnime listAnime : animeList) {
                JPanel animeCard = createAnimeCardFromListAnime(listAnime);
                resultsPanel.add(animeCard);
            }

            resultsPanel.revalidate();
            resultsPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error processing results", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createAnimeCardFromListAnime(ListAnime listAnime) {
        JPanel animeCard = new JPanel();
        animeCard.setLayout(new GridBagLayout());
        animeCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        animeCard.setPreferredSize(new Dimension(200, 250));

        Color[] colors = {Color.CYAN, Color.PINK, Color.ORANGE, Color.GREEN, Color.LIGHT_GRAY};
        animeCard.setBackground(colors[new Random().nextInt(colors.length)]);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel nameLabel = new JLabel(listAnime.getAnime().getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        animeCard.add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel message = new JLabel("Click to see the anime", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 15));
        message.setForeground(Color.WHITE);
        animeCard.add(message, gbc);
        
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(0, 64, 128));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        deleteButton.addActionListener(e -> deleteAnimeFromList(listAnime.getUser().getId(), listAnime.getAnime().getId(), animeCard));
        animeCard.add(deleteButton, gbc);
        
        animeCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        animeCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(listAnime.getAnime().getUrl()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error opening URL", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return animeCard;
    }
    
    private void deleteAnimeFromList(Long userId, Long animeId, JPanel animeCard) {
        try {
            String urlString = "http://localhost:8080/userListAnimes?userId=" + userId + "&animeId=" + animeId;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 204) {
                JOptionPane.showMessageDialog(null, "Anime removed from your list!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                resultsPanel.remove(animeCard);
                resultsPanel.revalidate();
                resultsPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Error removing anime", "Error", JOptionPane.ERROR_MESSAGE);
            }

            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error removing anime", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}