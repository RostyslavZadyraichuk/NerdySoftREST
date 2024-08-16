document.addEventListener("DOMContentLoaded", () => {
    let errorMessage = document.querySelector("#error-message");
    let container = document.querySelector(".container");

    let borrowIdInput = document.querySelector("#borrow-id");
    let bookTitleInput = document.querySelector("#book-title");
    let bookAuthorInput = document.querySelector("#book-author");
    let memberNameInput = document.querySelector("#member-name");
    let submit = document.querySelector("#submit");
    let formId = document.querySelector("#form-id");
    let formBookTitle = document.querySelector("#form-book-title");
    let formBookAuthor = document.querySelector("#form-book-author");
    let formMember = document.querySelector("#form-member");

    let mode = "create";

    findAll();

    document.querySelector("#form").addEventListener('submit', e => {
        e.preventDefault();
        let idValue;
        let titleValue;
        let authorValue;
        let memberValue;

        switch (mode) {
            case "create":
                titleValue = bookTitleInput.value;
                authorValue = bookAuthorInput.value;
                memberValue = memberNameInput.value;
                createBorrow(titleValue, authorValue, memberValue).then(() => findAll());
                break;
            case "find":
                titleValue = bookTitleInput.value;
                memberValue = memberNameInput.value;
                if (titleValue === '' && memberValue === '') {
                    findAll();
                } else {
                    findBorrow(titleValue, memberValue);
                }
                break;
            case "delete":
                idValue = parseInt(borrowIdInput.value);
                deleteBorrow(idValue);
                break;
        }

    });

    document.querySelector("#create").addEventListener("click", e => {
        mode = "create";
        bookTitleInput.value = "";
        bookAuthorInput.value = "";
        memberNameInput.value = "";
        formId.classList.add("display-none");
        formBookTitle.classList.remove("display-none");
        formBookAuthor.classList.remove("display-none");
        formMember.classList.remove("display-none");
        submit.value = "Create"

        findAll();
    });

    document.querySelector("#find").addEventListener("click", e => {
        mode = "find";
        bookTitleInput.value = "";
        memberNameInput.value = "";
        formId.classList.add("display-none");
        formBookAuthor.classList.add("display-none");
        formBookTitle.classList.remove("display-none");
        formMember.classList.remove("display-none");
        submit.value = "Find"

        findAll();
    });

    document.querySelector("#delete").addEventListener("click", e => {
        mode = "delete";
        borrowIdInput.value = "";
        formId.classList.remove("display-none");
        formBookTitle.classList.add("display-none");
        formBookAuthor.classList.add("display-none");
        formMember.classList.add("display-none");
        submit.value = "Delete by id"

        findAll();
    });

    async function findAll() {
        let url = "/borrows/list";
        let response = await fetch(url, {
            method: "get"
        });
        let borrows = await response.json();

        renderBorrows(borrows);
    }

    function renderBorrows(borrows) {
        container.innerHTML = "";

        borrows.forEach(borrow => {
            container.innerHTML += getBorrowItem(borrow);
        });
    }

    function getBorrowItem(borrow) {
        return `<div id="borrow${borrow.id}" class="borrow" book-title="${borrow.book.title}" book-author="${borrow.book.author.name}" member="${borrow.member.name}">
            <span><b>ID:</b> ${borrow.id}</span>
            <span><b>Book Title:</b> ${borrow.book.title}</span>
            <span><b>Book Author:</b> ${borrow.book.author.name}</span>
            <span><b>Member:</b> ${borrow.member.name}</span>
        </div>`;
    }

    async function createBorrow(title, author, member) {
        let url = "/borrow/create";
        let borrow = {
            book: {
                title: title,
                author: {
                    name: author
                }
            },
            member: {
                name: member
            }
        };

        let response = await fetch(url, {
            method: "post",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(borrow)
        });

        await processResponse(response);
    }

    async function findBorrow(title, member) {
        let url = "/borrow/find?bookTitle=" + title +
            "&memberName=" + member;

        let response = await fetch(url, {
            method: "get"
        });

        if (response.ok === false) {
            container.innerHTML = "";
        } else {
            let borrows = await response.json();
            renderBorrows(borrows);
        }
    }

    async function deleteBorrow(id) {
        let url = "/borrow/delete";
        let borrow = {
            id: id
        };

        let response = await fetch(url, {
            method: "post",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(borrow)
        });

        await processResponse(response).then(() => findAll());
    }

    async function processResponse(response) {
        if (response.ok === false) {
            errorMessage.innerText = await response.text();
            borrowIdInput.classList.add("error");
            bookTitleInput.classList.add("error");
            bookAuthorInput.classList.add("error");
            memberNameInput.classList.add("error");
        } else {
            errorMessage.innerText = "";
            borrowIdInput.classList.remove("error");
            bookTitleInput.classList.remove("error");
            bookAuthorInput.classList.remove("error");
            memberNameInput.classList.remove("error");
            borrowIdInput.value = "";
            bookTitleInput.value = "";
            bookAuthorInput.value = "";
            memberNameInput.value = "";
        }
    }
});