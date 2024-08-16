document.addEventListener("DOMContentLoaded", () => {
    let errorMessage = document.querySelector("#error-message");
    let container = document.querySelector(".container");

    let bookTitleInput = document.querySelector("#book-title");
    let bookIdInput = document.querySelector("#book-id");
    let bookAuthorInput = document.querySelector("#book-author");
    let submit = document.querySelector("#submit");
    let formId = document.querySelector("#form-id");
    let formTitle = document.querySelector("#form-title");
    let formAuthor = document.querySelector("#form-author");

    let mode = "create-update";

    findAll();

    document.querySelector("#form").addEventListener('submit', e => {
        e.preventDefault();
        let idValue;
        let titleValue;
        let authorValue;

        switch (mode) {
            case "create-update":
                titleValue = bookTitleInput.value;
                idValue = bookIdInput.value;
                authorValue = bookAuthorInput.value;

                if (idValue === '') {
                    createBook(titleValue, authorValue).then(() => findAll());
                } else {
                    let amount = parseInt(document.querySelector(`#book${idValue}`).getAttribute("amount"));
                    if (titleValue === '') {
                        titleValue = document.querySelector(`#book${idValue}`).getAttribute("title");
                    }
                    if (authorValue === '') {
                        authorValue = document.querySelector(`#book${idValue}`).getAttribute("author-name");
                    }
                    updateBook(parseInt(idValue), titleValue, authorValue, amount).then(() => findAll());
                }
                break;
            case "find":
                titleValue = bookTitleInput.value;
                authorValue = bookAuthorInput.value;
                if (titleValue === '' && authorValue === '') {
                    findAll();
                } else {
                    findBook(titleValue, authorValue);
                }
                break;
            case "delete":
                idValue = parseInt(bookIdInput.value);
                deleteBook(idValue);
                break;
        }

    });

    document.querySelector("#create-update").addEventListener("click", e => {
        mode = "create-update";
        bookTitleInput.value = "";
        bookIdInput.value = "";
        bookAuthorInput.value = "";
        formTitle.classList.remove("display-none");
        formId.classList.remove("display-none");
        formAuthor.classList.remove("display-none");
        submit.value = "Create/Update"

        findAll();
    });

    document.querySelector("#find").addEventListener("click", e => {
        mode = "find";
        bookTitleInput.value = "";
        bookAuthorInput.value = "";
        formTitle.classList.remove("display-none");
        formAuthor.classList.remove("display-none");
        formId.classList.add("display-none");
        submit.value = "Find"

        findAll();
    });

    document.querySelector("#delete").addEventListener("click", e => {
        mode = "delete";
        bookIdInput.value = "";
        formTitle.classList.add("display-none");
        formAuthor.classList.add("display-none");
        formId.classList.remove("display-none");
        submit.value = "Delete by id"

        findAll();
    });

    document.querySelector("#distinct").addEventListener('click', () => {
        getDistinct();
    });

    document.querySelector("#borrowed-distinct").addEventListener('click', () => {
        getBorrowedDistinct();
    });

    async function findAll() {
        let url = "/books/list";
        let response = await fetch(url, {
            method: "get"
        });
        let books = await response.json();

        renderBooks(books);
    }

    function renderBooks(books) {
        container.innerHTML = "";

        books.forEach(book => {
            container.innerHTML += getBookItem(book);
        });
    }

    function getBookItem(book) {
        return `<div id="book${book.id}" class="book" title="${book.title}" author-name="${book.author.name}" amount="${book.amount}">
            <span><b>ID:</b> ${book.id}</span>
            <span><b>Title:</b> ${book.title}</span>
            <span><b>Author:</b> ${book.author.name}</span>
            <span><b>Amount:</b> ${book.amount}</span>
        </div>`;
    }

    async function createBook(title, author) {
        let url = "/book/create";
        let member = {
            title: title,
            author: {
                name: author
            }
        };

        let response = await fetch(url, {
            method: "post",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(member)
        });

        await processResponse(response);
    }

    async function updateBook(id, title, author, amount) {
        let url = "/book/update";
        let book = {
            id: id,
            title: title,
            author: {
                name: author
            },
            amount: amount
        };

        let response = await fetch(url, {
            method: "post",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        });

        await processResponse(response);
    }

    async function findBook(title, author) {
        let url = "/book/find?title=" + title +
            "&author=" + author;

        let response = await fetch(url, {
            method: "get"
        });

        if (response.ok === false) {
            container.innerHTML = "";
        } else {
            let books = await response.json();
            renderBooks(books);
        }
    }

    async function deleteBook(id) {
        let url = "/book/delete";
        let book = {
            id: id
        };

        let response = await fetch(url, {
            method: "post",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        });

        await processResponse(response).then(() => findAll());
    }

    async function getDistinct() {
        let url = "/book/distinct";

        let response = await fetch(url, {
            method: "get"
        });

        if (response.ok === false) {
            container.innerHTML = "";
        } else {
            let books = await response.json();
            renderDistinctBooks(books);
        }
    }

    function renderDistinctBooks(books) {
        container.innerHTML = "";

        books.forEach(book => {
            container.innerHTML += getDistinctBookItem(book);
        });
    }

    function getDistinctBookItem(book) {
        return `<div class="distinct-book" title="${book.title}">
            <span><b>Title:</b> ${book.title}</span>
        </div>`;
    }

    async function getBorrowedDistinct() {
        let url = "/book/distinct/borrowed";

        let response = await fetch(url, {
            method: "get"
        });

        if (response.ok === false) {
            container.innerHTML = "";
        } else {
            let books = await response.json();
            renderBorrowedBooks(books);
        }
    }

    function renderBorrowedBooks(books) {
        container.innerHTML = "";

        books.forEach(book => {
            container.innerHTML += getBorrowedBookItem(book);
        });
    }

    function getBorrowedBookItem(book) {
        return `<div class="borrowed-book" title="${book.title}" borrowed-count="${book.amount}">
            <span><b>Title:</b> ${book.title}</span>
            <span><b>Borrowed Count:</b> ${book.amount}</span>
        </div>`;
    }

    async function processResponse(response) {
        if (response.ok === false) {
            errorMessage.innerText = await response.text();
            bookIdInput.classList.add("error");
            bookTitleInput.classList.add("error");
            bookAuthorInput.classList.add("error");
        } else {
            errorMessage.innerText = "";
            bookIdInput.classList.remove("error");
            bookTitleInput.classList.remove("error");
            bookAuthorInput.classList.remove("error");
            bookIdInput.value = "";
            bookTitleInput.value = "";
            bookAuthorInput.value = "";
        }
    }
});