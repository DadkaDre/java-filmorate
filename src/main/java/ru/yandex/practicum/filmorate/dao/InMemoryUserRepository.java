package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserRepository implements UserRepository {

   private final Map<Long, User> users = new HashMap<>();
   private final Map<Long, Set<Long>> friends = new HashMap<>();
   long currentId = 0;

   @Override
   public Collection<User> getAllUsers() {
      return users.values();
   }

   @Override
   public User get(Long id) {
      return users.get(id);
   }

   @Override
   public List<User> allFriends(Long id) {
      Set<Long> setOfFriends = friends.get(id);
      if (setOfFriends == null) {
         return new ArrayList<>();
      }

      ArrayList<User> listOfFriends = new ArrayList<>();
      for (Long userId : setOfFriends) {
         User user = users.get(userId);
         listOfFriends.add(user);
      }
      return listOfFriends;
   }

   @Override
   public User create(User user) {
      user.setId(getNextId());
      checkFieldsUser(user);
      users.put(user.getId(), user);
      return user;
   }

   @Override
   public User update(User user) {
      return checkUpdateFields(user);
   }

   @Override
   public void addFriend(Long id, Long friendId) {
      Set<Long> userFriends = friends.computeIfAbsent(id, k -> new HashSet<>());
      userFriends.add(friendId);

      Set<Long> userFriends2 = friends.computeIfAbsent(friendId, k -> new HashSet<>());
      userFriends2.add(id);
   }

   @Override
   public void deleteFriend(Long id, Long friendId) {
      Set<Long> userFriends = friends.computeIfAbsent(id, k -> new HashSet<>());
      userFriends.remove(friendId);

      Set<Long> userFriends2 = friends.computeIfAbsent(friendId, k -> new HashSet<>());
      userFriends2.remove(id);
   }

   @Override
   public List<User> mutualFriends(Long id, Long friendId) {
      Set<Long> userFriends = friends.get(id);
      Set<Long> otherFriends = friends.get(friendId);

      Set<Long> commonFriends = new HashSet<>(userFriends);
      commonFriends.retainAll(otherFriends);

      ArrayList<User> listOfFriends = new ArrayList<>();
      for (Long userId : commonFriends) {
         User user = users.get(userId);
         listOfFriends.add(user);
      }
      return listOfFriends;
   }

   public void checkFieldsUser(User user) {
      if (user.getName() == null) {
         user.setName(user.getLogin());
      }
   }

   public User checkUpdateFields(User user) {
      User oldUser = users.get(user.getId());
      if (user.getBirthday() != null) {
         oldUser.setBirthday(user.getBirthday());
      }
      if (user.getName() != null) {
         oldUser.setName(user.getName());
      }
      if (user.getEmail() != null) {
         oldUser.setEmail(user.getEmail());
      }
      if (user.getLogin() != null) {
         oldUser.setLogin(user.getLogin());
      }
      return oldUser;
   }

   @Override
   public void checkUserByID(Long id) {
      if (users.get(id) == null) {
         throw new NotFoundException("Пользователь по id: " + id + " не существует");
      }
   }

   public Long getNextId() {
      return ++currentId;
   }
}
