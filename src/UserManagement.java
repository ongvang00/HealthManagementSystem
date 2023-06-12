import java.util.HashSet;
import java.util.Set;

class UserManagement {
    private User currentUser;
    private Set<String> existingUsernames;

    public UserManagement() {
        currentUser = null;
        existingUsernames = new HashSet<>();
    }

    public boolean createUser(String username) {
        if (existingUsernames.contains(username)) {
            return false;
        }
        User user = new User(username);
        currentUser = user;
        existingUsernames.add(username);
        return true;
    }

    public boolean login(String username) {
        if (existingUsernames.contains(username)) {
            User user = new User(username);
            currentUser = user;
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
