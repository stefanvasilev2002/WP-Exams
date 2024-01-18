import axios from '../custom-axios/axios';

const EShopService = {
    fetchManufacturers: () => {
        return axios.get("/manufacturers");
    },
    fetchCategories: () => {
        return axios.get("/categories");
    },
    fetchProducts: () => {
        return axios.get("/products");
    },
    deleteProduct: (id) => {
        return axios.delete(`/products/delete/${id}`);
    },
    addProduct: (name, price, quantity, category, manufacturer) => {
        return axios.post("/products/add", {
            "name" : name,
            "price" : price,
            "quantity" : quantity,
            "category" : category,
            "manufacturer" : manufacturer
        });
    },
    editProduct: (id, name, price, quantity, category, manufacturer) => {
        return axios.put(`/products/edit/${id}`, {
            "name" : name,
            "price" : price,
            "quantity" : quantity,
            "category" : category,
            "manufacturer" : manufacturer
        });
    },
    getProduct: (id) => {
        return axios.get(`/products/${id}`);
    }
}

export default EShopService;
