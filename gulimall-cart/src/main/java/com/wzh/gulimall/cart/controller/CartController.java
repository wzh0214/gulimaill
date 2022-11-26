package com.wzh.gulimall.cart.controller;

import com.wzh.gulimall.cart.interceptor.CartInterceptor;
import com.wzh.gulimall.cart.service.CartService;
import com.wzh.gulimall.cart.vo.Cart;
import com.wzh.gulimall.cart.vo.CartItem;
import com.wzh.gulimall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author wzh
 * @data 2022/11/23 -16:29
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 获取购物项
     */
    @GetMapping("/currentUserCartItems")
    @ResponseBody // feign远程调用，传json数据，要加ResponseBody
    public List<CartItem> getCurrentUserCartItems() {
        List<CartItem> items = cartService.getUserCartItems();
        return items;
    }



    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
       Cart cart = cartService.getCart();
       model.addAttribute("cart", cart);
       return "cartList";
    }

    /**
     * 添加商品到购物车
     *  attributes.addFlashAttribute():将数据放在session中，可以在页面中取出，但是只能取一次
     *  attributes.addAttribute():将数据放在url后面
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                            RedirectAttributes ra) throws ExecutionException, InterruptedException {

        CartItem cartItem = cartService.addToCart(skuId, num);

        // 重定向携带数据，会在重定向路径后面自动添加?skuId:
        ra.addAttribute("skuId", skuId);
        // 重定向，防止用户重复提交
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    /**
     * 跳转到成功页
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccess(@RequestParam("skuId") Long skuId, Model model) {
        // 重定向到成功页面，再次查询购物车数据
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);

        return "success";

    }

    /**
     * 商品是否选中
     * @param skuId
     * @param checked
     * @return
     */
    @GetMapping(value = "/checkItem")
    public String checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "checked") Integer checked) {

        cartService.checkItem(skuId,checked);

        return "redirect:http://cart.gulimall.com/cart.html";

    }


    /**
     * 改变商品数量
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping(value = "/countItem")
    public String countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num) {

        cartService.changeItemCount(skuId,num);

        return "redirect:http://cart.gulimall.com/cart.html";
    }


    /**
     * 删除商品信息
     * @param skuId
     * @return
     */
    @GetMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam("skuId") Integer skuId) {

        cartService.deleteIdCartInfo(skuId);

        return "redirect:http://cart.gulimall.com/cart.html";

    }




}
