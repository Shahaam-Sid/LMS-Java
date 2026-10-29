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
    },
    ISBN: value => {
        if (value.trim() && !isValidISBN(value.trim())) return "Invalid ISBN"
        return "";
    },
    title: value => {
        if (value.trim() && (value.trim().length < 3 || value.trim().length > 50)) return "Title must contain 3 - 50 characters"
        return "";
    },
    author: value => {
        if (value.trim() && (value.trim().length < 3 || value.trim().length > 30)) return "Author must contain 3 - 30 characters"
        return "";
    },
    genre: value => {
        if (value.trim() && (value.trim().length < 3 || value.trim().length > 20)) return "Title must contain 3 - 20 characters"
        return "";
    },
    shelfLocation: value => {
        if (value.trim() && (value.trim().length < 3 || value.trim().length > 25)) return "Shelf Location must contain 3 - 25 characters"
        return "";
    },
    ID: value => {
        if (value.trim() && value.length != 9) return "Invalid ID";
        return "";
    }

}

function isOnlyDigit(str) {
    return /^\d+$/.test(str);
}

function isAlphaAndSpaces(str) {
    return /^[a-zA-Z ]+$/.test(str);
}

function isValidISBN(isbn) {
    if (typeof isbn !== 'string') return false;
    
    const n = isbn.length;
    if (n === 10) {
        return isValidISBN10(isbn);
    } else if (n === 13) {
        return isValidISBN13(isbn);
    }
    return false;
}

function isValidISBN10(isbn) {
    let sum = 0;
    for (let i = 0; i < 9; i++) {
        const digit = isbn.charCodeAt(i) - 48; // 48 is ASCII code for '0'
        if (digit < 0 || digit > 9) {
            return false;
        }
        sum += digit * (10 - i);
    }

    const lastChar = isbn.charAt(9);
    if (lastChar !== 'X' && (lastChar < '0' || lastChar > '9')) {
        return false;
    }

    sum += (lastChar === 'X') ? 10 : (lastChar.charCodeAt(0) - 48);
    return sum % 11 === 0;
}

function isValidISBN13(isbn) {
    let sum = 0;
    for (let i = 0; i < 12; i++) {
        const digit = isbn.charCodeAt(i) - 48;
        if (digit < 0 || digit > 9) {
            return false;
        }
        sum += (i % 2 === 0) ? digit : digit * 3;
    }

    const checkDigit = isbn.charCodeAt(12) - 48;
    if (checkDigit < 0 || checkDigit > 9) {
        return false;
    }

    return (sum + checkDigit) % 10 === 0;
}