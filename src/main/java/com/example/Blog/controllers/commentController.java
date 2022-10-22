package com.example.Blog.controllers;

import com.example.Blog.models.Comment;
import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import com.example.Blog.repo.CommentRepository;
import com.example.Blog.repo.PostRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class commentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @PostMapping("/createComment")
    public String createComment(@ModelAttribute("comment")
                                @Valid Comment comment,
                                BindingResult bindingResult,
                                HttpSession session,
                                @RequestParam Long id_post,
                                Model model) {

        if (bindingResult.hasErrors()) {
            session.setAttribute("org.springframework.validation.BindingResult.comment", bindingResult);
            session.setAttribute("comment", comment);
            session.setAttribute("flash", true);
            return "redirect:/selectedPost?id=" + String.valueOf(id_post);
        }

        Post post = postRepository.findById(id_post).get();
        Comment _comment = new Comment(comment.getText().trim(), new Date(), 0, 0, post);
        commentRepository.save(_comment);

        return "redirect:/selectedPost?id=" + String.valueOf(id_post);
    }

    @PostMapping("/comment/editCommentPage")
    public String toEditCommentPage(@RequestParam long id_comment,
                                    Model model) throws UnsupportedEncodingException {
        Comment comment = commentRepository.findById(id_comment).get();
        model.addAttribute("comment", comment);

        return "editComment";
    }


//    @RequestMapping(value = "/imageDisplay", method = RequestMethod.GET)
//    public void showImage(@RequestParam("id") Integer itemId, HttpServletResponse response,HttpServletRequest request)
//            throws ServletException, IOException {
//
//
//        Item item = itemService.get(itemId);
//        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
//        response.getOutputStream().write(item.getItemImage());
//
//
//        response.getOutputStream().close();
//    }


    @PostMapping(value = "/comment/edit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String toPostAfterCommentEdit(@ModelAttribute("comment")
                                         @Valid Comment comment,
                                         BindingResult bindingResult,
                                         @RequestParam("photo") MultipartFile photo,
                                         Model model) throws IOException {

        System.out.println(this.getClass());
        if (bindingResult.hasErrors()) {
            return "editComment";
        }

        Comment _comment = commentRepository.findById(comment.getId()).get();
        _comment.setText(comment.getText());

//        if (photo != null) {
////            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
////            String relativePath = "/images/fishPhoto/" + uuid + photo.getOriginalFilename();
////            String resultFilePath = "./src/main/resources/static" + relativePath;
////
////            File convertFile = new File(resultFilePath);
////            convertFile.createNewFile();
////            FileOutputStream newFishPhoto = new FileOutputStream(convertFile);
////            newFishPhoto.write(photo.getBytes());
////            newFishPhoto.close();
//
//            Path path = Paths.get("/images/fishPhoto/asdasd.png");
//            Files.write(path, bytes);
//
//            byte[] bArray = null;
//            try {
//                bArray = new byte[photo.getBytes().length];
//                int i = 0;
//                for (byte b : photo.getBytes()) {
//                    bArray[i++] = b;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            _comment.setPhoto(bArray);
//        }

        commentRepository.save(_comment);

        return "redirect:/selectedPost?id=" + _comment.getPost().getId();
    }

    @PostMapping("comment/delete")
    public String commentDelete(@RequestParam long id_post,
                                @RequestParam long id_comment,
                                Model model) {
        Comment comment = commentRepository.findById(id_comment).get();
        commentRepository.delete(comment);

        return "redirect:/selectedPost?id=" + id_post;
    }
}
