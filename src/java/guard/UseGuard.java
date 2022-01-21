/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guard;

import entities.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import repositories.UserRepository;
import variables.UserRole;

/**
 *
 * @author kaine
 */
public class UseGuard {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public UseGuard(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public boolean useRole(UserRole role) {
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("role");
        if (userRole == null || userRole.equals(role)) {
            return false;
        }

        return true;
    }

    public boolean useAuth() {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        try {
            if (userId == null) {
                session.invalidate();
                return false;
            }

            UserRepository userRepo = new UserRepository();
            User user = userRepo.getUserByUserId(userId);

            if (user == null) {
                session.invalidate();
                return false;
            }

            request.setAttribute("user", user);
        } catch (Exception e) {
            session.invalidate();
            return false;
        }

        return true;
    }
}
