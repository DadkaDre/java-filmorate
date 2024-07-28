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
   public List<User> allFriends(Long userId) {
      if (users.get(userId) == null) {
         throw new NotFoundException("Нет пользователя с таким id");
      }
      Set<Long> setOfFriends = friends.get(userId);
      if (setOfFriends == null) {
         return new ArrayList<User>();
      }

      ArrayList<User> listOfFriends = new ArrayList<>();
      for (Long id : setOfFriends) {
         User user = users.get(id);
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
      if (users.get(user.getId()) == null) {
         throw new NotFoundException("Фильм по id: " + user.getId() + " не найден.");
      }
      return checkUpdateFields(user);
   }

   @Override
   public void addFriend(Long userId, Long friendId) {
      if (users.get(userId) == null) {
         throw new NotFoundException("Нет пользователя с таким id");
      }
      if (users.get(friendId) == null) {
         throw new NotFoundException("Нет пользователя с таким id");
      }

      Set<Long> userFriends = friends.computeIfAbsent(userId, k -> new HashSet<>());
      userFriends.add(friendId);

      Set<Long> userFriends2 = friends.computeIfAbsent(friendId, k -> new HashSet<>());
      userFriends2.add(userId);
   }


   @Override
   public void deleteFriend(Long userId, Long friendId) {
      if (users.get(userId) == null) {
         throw new NotFoundException("Нет пользователя с таким id");
      }
      if (users.get(friendId) == null) {
         throw new NotFoundException("Нет пользователя с таким id");
      }
      Set<Long> userFriends = friends.computeIfAbsent(userId, k -> new HashSet<>());
      userFriends.remove(friendId);
      Set<Long> userFriends2 = friends.computeIfAbsent(friendId, k -> new HashSet<>());
      userFriends2.remove(userId);
   }

   @Override
   public List<User> mutualFriends(Long userId, Long otherUserId) {
      if (users.get(userId) == null) {
         throw new NotFoundException("Нет пользователя с таким id");
      }
      if (users.get(otherUserId) == null) {
         throw new NotFoundException("Нет пользователя с таким id");
      }
      Set<Long> userFriends = friends.get(userId);
      Set<Long> otherFriends = friends.get(otherUserId);

      Set<Long> commonFriends = new HashSet<>(userFriends);
      commonFriends.retainAll(otherFriends);

      ArrayList<User> listOfFriends = new ArrayList<>();
      for (Long id : commonFriends) {
         User user = users.get(id);
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

   public Long getNextId() {
      return ++currentId;
   }
}
