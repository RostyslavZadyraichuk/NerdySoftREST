document.addEventListener("DOMContentLoaded", () => {
    let errorMessage = document.querySelector("#error-message");
    let container = document.querySelector(".container");

    let memberNameInput = document.querySelector("#member-name");
    let memberIdInput = document.querySelector("#member-id");
    let submit = document.querySelector("#submit");
    let formId = document.querySelector("#form-id");
    let formName = document.querySelector("#form-name");

    let mode = "create-update";

    findAll();

    document.querySelector("#form").addEventListener('submit', e => {
        e.preventDefault();
        let idValue;
        let nameValue;

        switch (mode) {
            case "create-update":
                nameValue = memberNameInput.value;
                idValue = memberIdInput.value;

                if (idValue === '') {
                    createMember(nameValue).then(() => findAll());
                } else {
                    let membershipDate = document.querySelector(`#member${idValue}`).getAttribute("membership-date");
                    updateMember(parseInt(idValue), nameValue, membershipDate).then(() => findAll());
                }
                break;
            case "find":
                nameValue = memberNameInput.value;
                if (nameValue === '') {
                    findAll();
                } else {
                    findByName(nameValue);
                }
                break;
            case "delete":
                idValue = parseInt(memberIdInput.value);
                deleteMember(idValue);
                break;
        }

    });

    document.querySelector("#create-update").addEventListener("click", e => {
        mode = "create-update";
        memberNameInput.value = "";
        memberIdInput.value = "";
        formName.classList.remove("display-none");
        formId.classList.remove("display-none");
        submit.value = "Create/Update"

        findAll();
    });

    document.querySelector("#find").addEventListener("click", e => {
        mode = "find";
        memberNameInput.value = "";
        formName.classList.remove("display-none");
        formId.classList.add("display-none");
        submit.value = "Find by name"

        findAll();
    });

    document.querySelector("#delete").addEventListener("click", e => {
        mode = "delete";
        memberIdInput.value = "";
        formName.classList.add("display-none");
        formId.classList.remove("display-none");
        submit.value = "Delete by id"

        findAll();
    });

    async function findAll() {
        let url = "/members/list";
        let response = await fetch(url, {
            method: "get"
        });
        let members = await response.json();

        renderMembers(members);
    }

    function renderMembers(members) {
        container.innerHTML = "";

        members.forEach(member => {
            container.innerHTML += getMemberItem(member);
        });
    }

    function getMemberItem(member) {
        return `<div id="member${member.id}" class="member" name="${member.name}" membership-date="${member.membershipDate}">
            <span><b>ID:</b> ${member.id}</span>
            <span><b>Name:</b> ${member.name}</span>
            <span><b>MembershipDate:</b> ${member.membershipDate}</span>
        </div>`;
    }

    async function createMember(name) {
        let url = "/member/create";
        let member = {
            name: name
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

    async function updateMember(id, name, membershipDate) {
        let url = "/member/update";
        let member = {
            id: id,
            name: name,
            membershipDate: membershipDate
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

    async function findByName(name) {
        let url = "/member/findByName?name=" + name;

        let response = await fetch(url, {
            method: "get"
        });

        if (response.ok === false) {
            container.innerHTML = "";
        } else {
            let member = await response.json();
            renderMembers([member]);
        }
    }

    async function deleteMember(id) {
        let url = "/member/delete";
        let member = {
            id: id
        };

        let response = await fetch(url, {
            method: "post",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(member)
        });

        await processResponse(response).then(() => findAll());
    }

    async function processResponse(response) {
        if (response.ok === false) {
            errorMessage.innerText = await response.text();
            memberIdInput.classList.add("error");
            memberNameInput.classList.add("error");
        } else {
            errorMessage.innerText = "";
            memberIdInput.classList.remove("error");
            memberNameInput.classList.remove("error");
            memberIdInput.value = "";
            memberNameInput.value = "";
        }
    }
});