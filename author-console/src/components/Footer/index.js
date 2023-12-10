import React from "react";
import "./index.css";

const footerLinks = [
    "About YagaTalk",
    "Terms of Service",
    "Privacy Policy",
    "Contact Us"
]

export function Footer() {
    return (
        <div className="Footer">
            <div className="footer-links">
                {footerLinks.map((link) => <a className="footer-link" key={link} href={link}>{link}</a>)}
            </div>
        </div>
    )
}