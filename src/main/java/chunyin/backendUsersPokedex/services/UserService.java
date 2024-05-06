package chunyin.backendUsersPokedex.services;

import chunyin.backendUsersPokedex.dao.UserDAO;
import chunyin.backendUsersPokedex.entities.User;
import chunyin.backendUsersPokedex.exceptions.BadRequestException;
import chunyin.backendUsersPokedex.exceptions.NotFoundException;
import chunyin.backendUsersPokedex.payloads.NewUserDTO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public User save(NewUserDTO body) throws IOException {
        userDAO.findByEmail(body.email()).ifPresent(
                utente -> {
                    throw  new BadRequestException("The email " + body.email() + " is already taken");
                }
        );
        User utente = new User(body.username(), bcrypt.encode(body.password()), body.email(), body.name(), body.surname(), "https://ui-avatars.com/api/?name="+ body.name().charAt(0) + "+" + body.surname().charAt(0));
        return userDAO.save(utente);
    }

    public Page<User> getUser(int page, int size, String sort){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userDAO.findAll(pageable);
    }

    public User findById(UUID id){
        return userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByIdAndUpdate(UUID id, User body){
        User found = this.findById(id);
        found.setName(body.getName());
        found.setSurname(body.getSurname());
        found.setEmail(body.getEmail());
        found.setPassword(body.getPassword());
        found.setUsername(body.getUsername());
        found.setAvatarUrl("https://ui-avatars.com/api/?name=" + body.getName().charAt(0) + "+" + body.getSurname().charAt(0));
        return userDAO.save(found);
    }

    public void findByIDAndDelete(UUID id) {
        User found = this.findById(id);
        userDAO.delete(found);
    }

    public User uploadAvatar(UUID id, MultipartFile file) throws IOException{
        User found = this.findById(id);
        String avatarUrl = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatarUrl(avatarUrl);
        return userDAO.save(found);
    }

    public User findByEmail(String email){
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));
    }
}
