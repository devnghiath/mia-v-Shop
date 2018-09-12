package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.models.entity.Inventory;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/inventory")
public class AInventoryView extends AdminControllerBase {
    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public String home(Model model){
        List<Inventory> inventories = dataBaseService.getAll(Inventory.class);
        model.addAttribute("list", inventories);
        return contentPage("list/inventory", model);
    }
}
