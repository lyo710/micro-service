<script>
    function addForm() {
        $("#appUpdateForm").reset();
    }

    function updateForm(appId, appName, appContacts, contactsEmail) {
        $("#appId").val(appId);
        $("#appName").val(appName);
        $("#appContacts").val(appContacts);
        $("#contactsEmail").val(contactsEmail);
    }

    function deleteAppInfo(appId) {
        self.location = '${request.contextPath}/service-governance/console/provider/delete/' + appId;
    }

    function submitForm() {
        if ($("#appId").val()) {
            $("#appUpdateForm").submit();
        } else {
            alert("app id required!!")
        }
    }
</script>

<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">Service Provider List</div>

    <!-- Table -->
    <table class="table">
        <thead>
        <tr>
            <th>Provider ID</th>
            <th>Provider Name</th>
            <th>Provider Contacts</th>
            <th>Contacts Email</th>
            <th>
                <button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal"
                        onclick="javascript:addForm()">
                    Add
                </button>
            </th>
        </tr>
        </thead>
        <tbody>
        <#list providers as provider>
        <tr>
            <td>${provider.id}</td>
            <td>${provider.name?default("")}</td>
            <td>${provider.contacts?default("")}</td>
            <td>${provider.email?default("")}</td>
            <th scope="row">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal"
                        onclick="javascript:updateForm(${provider.id},'${provider.name?default("")}','${provider.contacts?default("")}','${provider.email?default("")}')">
                    Update
                </button>
                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModal"
                        onclick="javascript:deleteAppInfo(${provider.id})">
                    Delete
                </button>
            </th>
        </tr>
        </#list>
        </tbody>
    </table>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Update App Info</h4>
            </div>
            <div class="modal-body">
                <form id="appUpdateForm" action="${request.contextPath}/service-governance/console/provider/persist"
                      method="post">
                    <div class="form-group">
                        <label for="appId">Id</label>
                        <input type="number" class="form-control" id="appId" name="id" placeholder="appId" required>
                    </div>
                    <div class="form-group">
                        <label for="appName">Name</label>
                        <input type="text" class="form-control" id="appName" name="name" placeholder="appName">
                    </div>
                    <div class="form-group">
                        <label for="appContacts">Contacts</label>
                        <input type="text" class="form-control" id="appContacts" name="contacts"
                               placeholder="appContacts">
                    </div>
                    <div class="form-group">
                        <label for="contactsEmail">Email</label>
                        <input type="text" class="form-control" id="contactsEmail" name="email"
                               placeholder="contactsEmail">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" onclick="submitForm()">Save changes</button>
            </div>
        </div>
    </div>
</div>