/**
 * ShopNest — Online Shopping Store Client-Side Scripts
 */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Auto-dismiss floating alerts after 4.5 seconds
    const alerts = document.querySelectorAll(".sn-alert-floating");
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = "opacity 0.5s ease";
            alert.style.opacity = "0";
            setTimeout(function () {
                alert.remove();
            }, 500);
        }, 4500);
    });

    // 2. Quantity Stepper (+ and -) for Product Details & Cart
    document.querySelectorAll(".sn-qty-group").forEach(function (group) {
        const btnMinus = group.querySelector(".btn-minus");
        const btnPlus = group.querySelector(".btn-plus");
        const input = group.querySelector(".sn-qty-input");

        if (btnMinus && input) {
            btnMinus.addEventListener("click", function () {
                let val = parseInt(input.value) || 1;
                if (val > 1) {
                    input.value = val - 1;
                    triggerUpdate(input);
                }
            });
        }

        if (btnPlus && input) {
            btnPlus.addEventListener("click", function () {
                let val = parseInt(input.value) || 1;
                let max = parseInt(input.getAttribute("max")) || 99;
                if (val < max) {
                    input.value = val + 1;
                    triggerUpdate(input);
                }
            });
        }
    });

    function triggerUpdate(input) {
        // If inside an auto-submitting cart form
        const form = input.closest("form.auto-update-form");
        if (form) {
            form.submit();
        }
    }

    // 3. Image URL live preview for Admin Product Form
    const imgUrlInput = document.getElementById("productImageUrlInput");
    const imgPreview = document.getElementById("productImagePreview");
    if (imgUrlInput && imgPreview) {
        imgUrlInput.addEventListener("input", function () {
            const url = this.value.trim();
            if (url) {
                imgPreview.src = url;
                imgPreview.style.display = "block";
            }
        });
    }
});

// Confirmation dialog helper for deleting items or clearing cart
function confirmAction(message) {
    return confirm(message || "Are you sure you want to proceed?");
}

// Print order invoice receipt
function printReceipt() {
    window.print();
}
