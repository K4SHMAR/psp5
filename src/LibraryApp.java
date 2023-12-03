import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryApp extends JFrame {
    private List<Book> bookList;
    private JList<Book> bookJList;
    private DefaultListModel<Book> bookListModel;
    private JTextField titleTextField;
    private JComboBox<String> genreComboBox;
    private JTextArea descriptionTextArea;

    public LibraryApp() {
        // Загрузка данных из файла при запуске приложения
        loadBooksFromFile("books.txt");

        setTitle("Library App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Создание кнопки "Добавить книгу"
        JButton addButton = new JButton("Добавить книгу");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        // Создание списка книг
        bookListModel = new DefaultListModel<>();
        bookJList = new JList<>(bookListModel);

        // Создание текстовых полей и текстовой области
        titleTextField = new JTextField();
        genreComboBox = new JComboBox<>(new String[]{"Фантастика", "Детектив", "Роман"});
        descriptionTextArea = new JTextArea();

        // Создание кнопки "Удалить книгу"
        JButton deleteButton = new JButton("Удалить книгу");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        // Создание панели для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        add(new JScrollPane(bookJList), BorderLayout.WEST);
        add(createFormPanel(), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Загрузка списка книг в JList
        updateBookList();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addBook() {
        String title = titleTextField.getText();
        String genre = genreComboBox.getSelectedItem().toString();
        String description = descriptionTextArea.getText();
        Book book = new Book(title, genre, description);
        bookList.add(book);
        updateBookList();
        clearForm();
    }

    private void deleteBook() {
        int selectedIndex = bookJList.getSelectedIndex();
        if (selectedIndex != -1) {
            bookList.remove(selectedIndex);
            updateBookList();
            clearForm();
        }
    }

    private void updateBookList() {
        bookListModel.clear();
        for (Book book : bookList) {
            bookListModel.addElement(book);
        }
    }

    private void clearForm() {
        titleTextField.setText("");
        genreComboBox.setSelectedIndex(0);
        descriptionTextArea.setText("");
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));

        formPanel.add(new JLabel("Название книги:"));
        formPanel.add(titleTextField);
        formPanel.add(new JLabel("Жанр:"));
        formPanel.add(genreComboBox);
        formPanel.add(new JLabel("Описание:"));
        formPanel.add(new JScrollPane(descriptionTextArea));

        return formPanel;
    }

    private void loadBooksFromFile(String fileName) {
        bookList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0];
                    String genre = parts[1];
                    String description = parts[2];
                    Book book = new Book(title, genre, description);
                    bookList.add(book);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooksToFile(String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (Book book : bookList) {
                writer.write(book.getTitle() + "," + book.getGenre() + "," + book.getDescription());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        // Сохранение данных в файл при закрытии окна
        saveBooksToFile("books.txt");
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibraryApp();
            }
        });
    }
}

class Book {
    private String title;
    private String genre;
    private String description;

    public Book(String title, String genre, String description) {
        this.title = title;
        this.genre = genre;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return title;
    }
}