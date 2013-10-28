package testcase.model;

import java.util.*;

public class User {
    private String name;
    private int status;
    private Date birthday;
    private boolean locked;
    private List<Book> bookList;

    public static User newInstance() {
        User user = new User();
        List<Book> bookList = new ArrayList<Book>();
        bookList.add(new Book("JavaBook", 10.99, user));
        bookList.add(new Book("DotNetBook", 99.00, user));
        bookList.add(new Book("JavaScriptBook", 20.10, user));

        user.name = "Jack";
        user.status = 0;
        user.birthday = new Date();
        user.locked = false;
        user.bookList = bookList;
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Book find(String name) {
        return new Book(name, 99.99, this);
    }

    public Book notFind(String name) {
        return null;
    }

    public Book getNone() {
        return null;
    }
}
