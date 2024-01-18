import React from 'react';
import {useHistory} from 'react-router-dom';

const ProductAdd = (props) => {

    const history = useHistory();
    const [formData, updateFormData] = React.useState({
        name: "",
        price: 0,
        quantity: 0,
        category: 1,
        manufacturer: 1
    })

    const handleChange = (e) => {
        updateFormData({
            ...formData,
            [e.target.name]: e.target.value.trim()
        })
    }

    const onFormSubmit = (e) => {
        e.preventDefault();
        const name = formData.name;
        const price = formData.price;
        const quantity = formData.quantity;
        const category = formData.category;
        const manufacturer = formData.manufacturer;

        props.onAddProduct(name, price, quantity, category, manufacturer);
        history.push("/products");
    }

    return(
        <div className="row mt-5">
            <div className="col-md-5">
                <form onSubmit={onFormSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Product name</label>
                        <input type="text"
                               className="form-control"
                               id="name"
                               name="name"
                               required
                               placeholder="Enter product name"
                               onChange={handleChange}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="price">Price</label>
                        <input type="text"
                               className="form-control"
                               id="price"
                               name="price"
                               placeholder="Price"
                               required
                               onChange={handleChange}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="quantity">Quantity</label>
                        <input type="text"
                               className="form-control"
                               id="quantity"
                               name="quantity"
                               placeholder="Quantity"
                               required
                               onChange={handleChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Category</label>
                        <select name="category" className="form-control" onChange={handleChange}>
                            {props.categories.map((term) =>
                                <option value={term.id}>{term.name}</option>
                            )}
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Manufacturer</label>
                        <select name="manufacturer" className="form-control" onChange={handleChange}>
                            {props.manufacturers.map((term) =>
                                <option value={term.id}>{term.name}</option>
                            )}
                        </select>
                    </div>
                    <button id="submit" type="submit" className="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    )
}

export default ProductAdd;