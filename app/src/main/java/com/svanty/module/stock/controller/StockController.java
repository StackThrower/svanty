package com.svanty.module.stock.controller;

import com.svanty.constans.GlobalConstants;
import com.svanty.db.page.Paged;
import com.svanty.module.core.db.entity.User;
import com.svanty.module.core.model.UserModel;
import com.svanty.module.stock.db.entity.Categories;
import com.svanty.module.stock.db.entity.Stock;
import com.svanty.module.stock.exception.ImageNotFoundException;
import com.svanty.module.stock.model.*;
import com.svanty.module.stock.service.ImagesService;
import com.svanty.module.stock.service.UsersService;
import com.svanty.security.UserDetailsSecurity;
import com.svanty.security.validator.AccountSettingsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.Locale;

@Controller
@RequestMapping("{locale}")
public class StockController {

    @Autowired
    MessageSource messageSource;

    /**
     * Image service
     */
    private ImagesService imagesService;

    /**
     * Users service
     */
    private UsersService usersService;

    /**
     * Uploads file configuration path
     */
    @Value("${app.upload.path}")
    private String uploadRootPath;

    /**
     * Main constructor
     *
     * @param imagesService
     */
    @Autowired
    StockController(ImagesService imagesService, UsersService usersService) {
        this.imagesService = imagesService;
        this.usersService = usersService;
    }

    /**
     * Get image details
     *
     * @param id
     * @param slug
     * @param model
     * @return
     */
    @GetMapping("/photo/{id}/{slug}")
    public String photo(@PathVariable Integer id, @PathVariable String slug, Model model) {

        ImageDetailModel image;
        try {
            image = imagesService.getDetailsById(id);
        } catch (ImageNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "image not found");
        }

        Locale locale = LocaleContextHolder.getLocale();
        String titleTemplate = messageSource.getMessage("title.photo-template", new Object[0], locale);
        String title = String.format(titleTemplate, image.getTitle(), image.getId());

        model.addAttribute("image", image);
        model.addAttribute("title", title);

        return "module/stock/photo";
    }

    /**
     * Download file
     *
     * @param token
     * @param type
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/downloadImage")
    public ResponseEntity<InputStreamResource> download(@RequestParam("id") Integer id,
                                                        @RequestParam("type") String type, HttpServletRequest request) throws IOException {

        Stock.Type stockType = Stock.Type.valueOf(type);

        StockItemModel stockItemModel = imagesService.getStockById(id, stockType);

        File initialFile = new File(uploadRootPath + "/" + stockType + "/" + stockItemModel.getName());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(initialFile));


        imagesService.addDownload(stockItemModel.getImagesId(), request.getRemoteAddr(), stockItemModel.getType());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + stockItemModel.getFileName())
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .contentLength(initialFile.length())
                .body(resource);
    }

    /**
     * Premium photos
     *
     * @return
     */
    @GetMapping("/photos/premium")
    public String premium(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber, Model model) {

        Paged<ImageItemModel> images = imagesService
                .getPremiumImageItemPage(pageNumber,
                        GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);

        return "module/stock/premium";
    }

    /**
     * Latest photos
     *
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/latest")
    public String latest(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber, Model model) {

        Paged<ImageItemModel> images = imagesService
                .getLatestImageItemPage(pageNumber,
                        GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);

        return "module/stock/latest";
    }

    @GetMapping("/featured")
    public String featured(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "timeframe", required = false, defaultValue = "all") String timeframe,
            Model model) {

        Paged<ImageItemModel> images = imagesService.getFeaturedImageItemPage(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("timeframe", timeframe);

        return "module/stock/featured";
    }

    /**
     * Popular photos
     *
     * @return
     */
    @GetMapping("/popular")
    public String popular(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "timeframe", required = false, defaultValue = "all") String timeframe, Model model) {

        Paged<ImageItemModel> images = imagesService.getPopularImageItemPage(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("timeframe", timeframe);

        return "module/stock/popular";
    }

    /**
     * Most commented
     *
     * @return
     */
    @GetMapping("/most/commented")
    public String commented(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "timeframe", required = false, defaultValue = "all") String timeframe, Model model) {


        Paged<ImageItemModel> images = imagesService.getMostCommentedImageItemPage(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("timeframe", timeframe);

        return "module/stock/commented";
    }

    /**
     * Most viewed
     *
     * @return
     */
    @GetMapping("/most/viewed")
    public String viewed(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                         @RequestParam(value = "timeframe", required = false, defaultValue = "all") String timeframe, Model model) {

        Paged<ImageItemModel> images = imagesService.getMostViewedImageItemPage(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("timeframe", timeframe);

        return "module/stock/viewed";
    }

    /**
     * Most downloads
     *
     * @return
     */
    @GetMapping("/most/downloads")
    public String downloads(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "timeframe", required = false, defaultValue = "all") String timeframe, Model model) {

        Paged<ImageItemModel> images = imagesService.getMostViewedImageItemPage(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("timeframe", timeframe);


        return "module/stock/downloads";
    }

    /**
     * Get category
     *
     * @param slug
     * @return
     */
    @GetMapping("/category/{slug}")
    public String category(@PathVariable String slug,
                           @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                           Model model) {

        Categories category = imagesService.findCategoryWithSlug(slug);
        Paged<ImageItemModel> images = imagesService.getCategoryImageItemPage(category.getId(), pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);
        Integer itemCount = imagesService.findCountByCategory(category.getId());

        Locale locale = LocaleContextHolder.getLocale();
        String titleTemplate = messageSource.getMessage("title.category-template", new Object[0], locale);
        String title = String.format(titleTemplate, category.getName());

        model.addAttribute("title", title);
        model.addAttribute("images", images);
        model.addAttribute("category", category);
        model.addAttribute("itemCount", itemCount);

        return "module/stock/category";
    }

    /**
     * Get categories
     *
     * @return
     */
    @GetMapping("/categories")
    public String categories(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber, Model model) {

        Paged<CategoryItemModel> categories = imagesService.getCategories(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("categoriesList", categories);


        return "module/stock/categories";
    }

    /**
     * Members
     *
     * @return
     */
    @GetMapping("/members")
    public String members(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber, Model model) {

        Paged<UserModel> members = usersService.getUsersByPage(pageNumber, GlobalConstants.MEMBERS_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("members", members);
        model.addAttribute("membersCount", usersService.count());

        return "module/stock/members";
    }

    /**
     * Collections
     *
     * @return
     */
    @GetMapping("/collections")
    public String collections() {

        return "module/stock/collections";
    }

    /**
     * Tags
     *
     * @return
     */
    @GetMapping("/tag")
    public String tags(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                       Model model) {

        Paged<TagModel> tags = imagesService.getTags(pageNumber, GlobalConstants.TAGS_LIST_PAGE_MAX_COUNT);

        model.addAttribute("tags", tags);

        return "module/stock/tags";
    }

    /**
     * Get photos by tag
     *
     * @param slug
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/tag/{slug}")
    public String tag(@PathVariable String slug,
                      @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                      Model model) {

        Locale locale = LocaleContextHolder.getLocale();
        TagModel tag = imagesService.getTagBySlug(slug);

        if (!tag.isFound()) {
            tag = imagesService.getTagByTitle(slug);

            if (tag.isFound())
                return "redirect:/" + locale.getLanguage() + "/tag/" + tag.getSlug();
        }

        if (!tag.isFound()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "tag not found");
        }

        Paged<ImageItemModel> images =
                imagesService.getByTagImageItemPage(tag.getSlug(), pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        String titleTemplate = messageSource.getMessage("title.tag-template", new Object[0], locale);
        String title = String.format(titleTemplate, tag.getTitle().substring(0,1).toUpperCase() +
                tag.getTitle().substring(1), tag.getTitle());

        model.addAttribute("images", images);
        model.addAttribute("tag", tag);
        model.addAttribute("title", title);
        model.addAttribute("imagesCount", images.getPaging().getTotalElements());

        return "module/stock/tag";
    }

    /**
     * Get photos by camera
     *
     * @param camera
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/cameras/{camera}")
    public String cameras(@PathVariable String camera,
                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber, Model model) {

        Paged<ImageItemModel> images = imagesService.getMostViewedImageItemPage(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("camera", camera);

        return "module/stock/cameras";
    }

    /**
     * @param code
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/colors/{code}")
    public String colors(@PathVariable String code,
                         @RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNumber,
                         Model model) {

        Paged<ImageItemModel> images = imagesService.getMostViewedImageItemPage(pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        Locale locale = LocaleContextHolder.getLocale();
        String titleTemplate = messageSource.getMessage("title.color-template", new Object[0], locale);
        String title = String.format(titleTemplate, code);

        model.addAttribute("images", images);
        model.addAttribute("code", code);
        model.addAttribute("title", title);

        return "module/stock/colors";
    }

    /**
     * Search page
     *
     * @param query
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false, defaultValue = "") String query,
                         @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                         Model model) {

        Paged<ImageItemModel> images = imagesService.findImageItemPage(query, pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("query", query);

        return "module/stock/search";
    }


    @GetMapping("/account/{username}")
    public String account(@PathVariable String username,
                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber, Model model) {

        User account = usersService.getByUsername(username);

        Paged<ImageItemModel> images = imagesService.getByUserImageItemPage(account.getId(), pageNumber, GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        model.addAttribute("images", images);
        model.addAttribute("account", account);

        return "module/stock/profile";
    }

    @GetMapping("/settings/account")
    public String accountSettings(AccountSettingsForm accountSettingsForm, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsSecurity userDetailsSecurity = (UserDetailsSecurity) authentication.getPrincipal();
        User user = userDetailsSecurity.getUserDetails();

        accountSettingsForm.setName(user.getName());
        accountSettingsForm.setUsername(user.getUsername());
        accountSettingsForm.setEmail(user.getEmail());
        accountSettingsForm.setCity(user.getCity());
        accountSettingsForm.setPaypal_account(user.getPaypal_account());
        accountSettingsForm.setWebsite(user.getWebsite());
        accountSettingsForm.setFacebook(user.getFacebook());
        accountSettingsForm.setTwitter(user.getTwitter());
        accountSettingsForm.setInstagram(user.getInstagram());

        model.addAttribute("isSuccessful", false);

        return "module/stock/account-settings";
    }


    @PostMapping("/settings/account")
    public String accountSettingsPost(@Valid AccountSettingsForm accountSettingsForm,
                                      BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isSuccessful", false);
            return "module/stock/account-settings";
        }

        usersService.updateAccount(accountSettingsForm, uploadRootPath);

        model.addAttribute("isSuccessful", true);

        return "module/stock/account-settings";

    }


    @GetMapping("/settings/portfolio")
    public String portfolioSettings(@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNumber,
                                    Model model) {

        model.addAttribute("tags", null);

        return "module/stock/portfolio-settings";
    }




    /**
     * Upload page
     *
     * @param model
     * @return
     */
    @GetMapping("/upload")
    public String upload(Model model) {

        return "module/stock/upload";
    }

    /**
     * Upload page
     *
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPost(
            @RequestParam("title") String title,
            @RequestParam("tags") String tags,
            @RequestParam("categories_id") Integer categories_id,
            @RequestParam("how_use_image") String how_use_image,
            @RequestParam("attribution_required") String attribution_required,
            @RequestParam("description") String description,
            @RequestParam("photo") MultipartFile file) {

        String url = imagesService.uploadPost(uploadRootPath, file, title,
                description, categories_id, tags, how_use_image, attribution_required);

        return ResponseEntity.ok()
                .body("{\"success\":true,\"target\":\"" + url + "\"}");
    }


}
