package com.kreuterkeule.StudentSorter.controller;

import com.kreuterkeule.StudentSorter.dto.ChangeFiveMatesDto;
import com.kreuterkeule.StudentSorter.dto.UsernameDto;
import com.kreuterkeule.StudentSorter.model.UserEntity;
import com.kreuterkeule.StudentSorter.repository.UserRepository;
import com.kreuterkeule.StudentSorter.service.AuthenticatedUserService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RequestMapping("api/client")
@RestController
public class ClientController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    @GetMapping("changePriorityMate")
    public ResponseEntity<String> changePriorityMate(@RequestParam("username") String username) {
        UserEntity mate = userRepository.findByUsername(username);
        if (mate == null) {
            return new ResponseEntity<>("USER NON EXISTENT [" + username + "]", HttpStatus.BAD_REQUEST);
        }
        String clientUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity client = userRepository.findByUsername(clientUsername);
        if (client.getBlacklistedMates().containsValue(username)) {
            return new ResponseEntity<>("NEW PRIO MATE NOT SET, CAUSE IS CONTAINED IN BLACKLIST", HttpStatus.OK);
        }
        if (client.getFiveMatesPriorityName().containsValue(username)) {
            client.setPriorityMate(mate.getUsername());
            userRepository.save(client);
            return new ResponseEntity<>("NEW PRIORITY MATE SET SUCCESSFULLY [" + mate.getUsername() + "], BUT MATE IS CONTAINED IN FIVE MATES", HttpStatus.OK);
        }
        if (client == null) {
            return new ResponseEntity<>("USER NOT EXISTENT [" + client.getUsername() + "]", HttpStatus.BAD_REQUEST);
        }
        client.setPriorityMate(mate.getUsername());
        userRepository.save(client);
        return new ResponseEntity<>("NEW PRIORITY MATE SET SUCCESSFULLY [" + mate.getUsername() + "]", HttpStatus.OK);
    }

    @PostMapping("changeFiveMates")
    public ResponseEntity<String> changeFiveMates(@RequestBody ChangeFiveMatesDto changeFiveMatesDto) {
        boolean containsPriorityMate = false;
        Map<Integer, String> fiveMatesMap = new HashMap<>();
        String clientUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity client = userRepository.findByUsername(clientUsername);
        for (String username : changeFiveMatesDto.fiveMates) {
            if (client.getBlacklistedMates().containsValue(username)) {
                return new ResponseEntity<>("NOT SET, ONE USER FROM BLACKLIST", HttpStatus.OK); // handled by client
            }
            UserEntity mate = userRepository.findByUsername(username);
            if (mate == null) {
                return new ResponseEntity<>("USER NON EXISTENT [" + username + "]", HttpStatus.BAD_REQUEST);
            }
            System.out.println("MATE: " + username + " == PRIO " + client.getPriorityMate());
            System.out.println("BOOL ? " + (client.getPriorityMate().equals(username)));
            if (client.getPriorityMate().equals(username)) {
                containsPriorityMate = true;
            }
            fiveMatesMap.put(changeFiveMatesDto.fiveMates.indexOf(username), username);
        }
        if (client == null) {
            return new ResponseEntity<>("USER NOT EXISTENT [" + client.getUsername() + "]", HttpStatus.BAD_REQUEST);
        }
        client.setFiveMatesPriorityName(fiveMatesMap);
        userRepository.save(client);
        if (changeFiveMatesDto.fiveMates.size() > new HashSet<String>(changeFiveMatesDto.fiveMates).size()) {
            return new ResponseEntity<>("NEW MATES SET SUCCESSFULLY, BUT THEY CONTAIN DUPLICATES", HttpStatus.OK);
        }
        if (containsPriorityMate) {
            return new ResponseEntity<>("NEW MATES SET SUCCESSFULLY, BUT THEY CONTAIN THE PRIORITY MATE [" + client.getPriorityMate() + "]", HttpStatus.OK);
        }
        return new ResponseEntity<>("NEW MATES SET SUCCESSFULLY", HttpStatus.OK);
    }

    @GetMapping("addToBlacklist")
    public ResponseEntity<String> addToBlacklist(@RequestParam("username") String username) {
        UserEntity client = authenticatedUserService.getUserFromSecurityContext(SecurityContextHolder.getContext());
        if (userRepository.findByUsername(username) != null) {
            if (client.getFiveMatesPriorityName().containsValue(username)) {
                return new ResponseEntity<>("NOT SET, CAUSE USER IN FIVE MATES", HttpStatus.OK); // OK cause handled by client
            }
            if (client.getPriorityMate().equals(username)) {
                return new ResponseEntity<>("NOT SET, CAUSE USER IS PRIO USER", HttpStatus.OK); // OK cause handled by client
            }
            client.addToBlacklist(username);
            userRepository.save(client);
            return new ResponseEntity<>("ADDED [" + username + "] SUCCESSFULLY", HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR: USER NOT FOUND", HttpStatus.BAD_REQUEST);
    }

    // not sure may make errors maybe just use a reset function instead
    @GetMapping("deleteFromBlacklist") // not used yet
    public ResponseEntity<String> deleteFromBlacklist(@RequestParam("id") Integer id) {
        UserEntity client = authenticatedUserService.getUserFromSecurityContext(SecurityContextHolder.getContext());
        String toDelete = client.getBlacklistedMates().get(id - 1); // to delete user
        if (client != null) {
            client.deleteFromBlacklist(id);
            userRepository.save(client);
            return new ResponseEntity<>("SUCCESS DELETED [" + toDelete + "] FROM BLACKLIST", HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR: CLIENT NOT FOUND", HttpStatus.BAD_REQUEST);

    }

    @GetMapping("resetBlacklist")
    public ResponseEntity<String> resetBlacklist() {
        UserEntity client = authenticatedUserService.getUserFromSecurityContext(SecurityContextHolder.getContext());
        if (client != null) {
            client.setBlacklistedMates(new HashMap<>());
            userRepository.save(client);
            return new ResponseEntity<>("RESET SUCCESSFUL", HttpStatus.OK);
        }
        return new ResponseEntity<>("CLIENT NOT FOUND", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("changeBilingual")
    public ResponseEntity<String> changeBilingual(@RequestParam("state") String state) {
        UserEntity client = authenticatedUserService.getUserFromSecurityContext(SecurityContextHolder.getContext());
        if (client == null) {
            return new ResponseEntity<>("ERROR: USER NOT FOUND", HttpStatus.BAD_REQUEST);
        }
        if (state.equals("BILI")) {
            client.setBilingual(true);
        } else if (state.equals("NOT_BILI")) {
            client.setBilingual(false);
        } else {
            return new ResponseEntity<>("ERROR: INVALID STATE", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(client);
        return new ResponseEntity<>("CHANGED TO " + state, HttpStatus.OK);
    }

    @GetMapping("getUserInfo")
    public ResponseEntity<UserEntity> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity client = userRepository.findByUsername(username);
        if (client == null) {
            return new ResponseEntity<>(null /* client */, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @GetMapping("setWp")
    public ResponseEntity<String> setWp(@RequestParam("state") String wpSelect) {
        System.out.println(wpSelect);
        if (!wpSelect.equals("MINT") && !wpSelect.equals("KLAR")) { // .equals because you need that with strings
            return new ResponseEntity<>("ERROR: unknown state", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = authenticatedUserService.getUserFromSecurityContext(SecurityContextHolder.getContext());
        user.setWP(wpSelect);
        userRepository.save(user);
        return new ResponseEntity<>("SUCCESS: wp set to " + wpSelect, HttpStatus.OK);
    }

    @GetMapping("checkUser")
    public ResponseEntity<String> checkUser(@RequestParam("username") String username) {
        UserEntity requestUser = userRepository.findByUsername(username);
        if (requestUser == null) {
            return new ResponseEntity<>("false", HttpStatus.OK);
        }
        if (!requestUser.getRole().equals("USER")) {
            return new ResponseEntity<>("false", HttpStatus.OK);
        }
        return new ResponseEntity<>("true", HttpStatus.OK);
    }

    @GetMapping("done")
    public ResponseEntity<String> done() {
        UserEntity client = authenticatedUserService.getUserFromSecurityContext(SecurityContextHolder.getContext());
        client.setEnabled(false);
        userRepository.save(client);
        return new ResponseEntity<>("SUCCESS, YOU ARE NOW DISABLED", HttpStatus.OK);
    }

}
