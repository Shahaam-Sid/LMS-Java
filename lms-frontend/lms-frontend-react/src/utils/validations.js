export const validators = {
    name:  value => {
        if (value.trim() && !isAlphaAndSpaces(value.trim())) return "name must contain 3 - 35 letters";
        return "";
    },
    phone: value => {
        if (!isOnlyDigit(value)) return "phone must be exactly 11 digits";
        return "";
    },
    email: value => {
        const pattern = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        const emailVal = new RegExp(pattern);
        if (!emailVal.test(value)) return "Invalid Email"
        return "";
    },
    password: value => {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).+$/;
        if (!passwordRegex.test(value)) return `Invalid Password, Must contain: \n
One Uppercase Character \n
One Lowercase Character \n
One Special Character \n
One Numeric Character`

        return "";
    }

}

function isOnlyDigit(str) {
    return /^\d+$/.test(str);
}

function isAlphaAndSpaces(str) {
    return /^[a-zA-Z ]+$/.test(str);
}