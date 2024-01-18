import React from 'react';
import {Link} from 'react-router-dom';

const productTerm = (props) => {
    return (
        <tr>
            <td scope={"col"}>{props.term.name}</td>
            <td scope={"col"}>{props.term.price}</td>
            <td scope={"col"}>{props.term.quantity}</td>
            <td scope={"col"}>{props.term.category.name}</td>
            <td scope={"col"}>{props.term.manufacturer.name}</td>
            <td scope={"col"} className={"text-right"}>
                <a title={"Delete"} className={"btn btn-danger"}
                   onClick={() => props.onDelete(props.term.id)}>
                    Delete
                </a>
                <Link className={"btn btn-info ml-2"}
                      onClick={() => props.onEdit(props.term.id)}
                      to={`/products/edit/${props.term.id}`}>
                    Edit
                </Link>
            </td>
        </tr>
    )
}

export default productTerm;