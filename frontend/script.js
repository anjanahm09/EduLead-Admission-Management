const API="http://localhost:8080/api";
let leads=[];
let followUps=[];

document.addEventListener("DOMContentLoaded",()=>{loadAll();});

async function api(path,options={}){
    const res=await fetch(API+path,{headers:{"Content-Type":"application/json",...(options.headers||{})},...options});
    if(!res.ok){
        let message="Request failed";
        try{const data=await res.json();message=data.error||message;}catch(e){}
        throw new Error(message);
    }
    if(res.status===204)return null;
    return res.json();
}

async function loadAll(){
    try{
        leads=await api("/leads");
        followUps=await api("/followups");
        await loadDashboard();
        renderLeads();
        renderLeadPage();
        renderFollowUps();
        renderReports();
    }catch(e){alert("Backend connection error: "+e.message);}
}

async function loadDashboard(){
    const d=await api("/dashboard");
    document.getElementById("totalLeads").textContent=d.totalLeads;
    document.getElementById("newLeads").textContent=d.newLeads;
    document.getElementById("contactedLeads").textContent=d.contactedLeads;
    document.getElementById("convertedLeads").textContent=d.convertedLeads;
    document.getElementById("normalAgeing").textContent=d.normalAgeing;
    document.getElementById("ageing").textContent=d.ageing;
    document.getElementById("attentionRequired").textContent=d.attentionRequired;
    document.getElementById("followUpCount").textContent=followUps.filter(x=>x.status==="PENDING").length;
}

function showSection(name,button){
    ["dashboard","leads","followups","reports"].forEach(x=>document.getElementById(x+"Section").classList.add("hidden"));
    document.getElementById(name+"Section").classList.remove("hidden");
    document.querySelectorAll(".nav").forEach(x=>x.classList.remove("active"));
    if(button)button.classList.add("active");
    if(name==="reports")renderReports();
}

function ageInfo(date){
    if(!date)return {text:"Unknown",className:""};
    const days=Math.max(0,Math.floor((Date.now()-new Date(date).getTime())/86400000));
    if(days<=2)return {text:"Normal",className:"age-normal"};
    if(days<=7)return {text:"Ageing",className:"age-aging"};
    return {text:"Attention",className:"age-attention"};
}

function statusClass(status){
    if(status==="CONVERTED")return "status converted";
    if(status==="LOST"||status==="NOT_INTERESTED")return "status danger";
    return "status";
}

function leadRows(data){
    return data.map(l=>{
        const age=ageInfo(l.createdAt);
        return `<tr>
            <td>${l.id}</td><td>${esc(l.name)}</td><td>${esc(l.phone||"-")}</td>
            <td>${esc(l.course||"-")}</td><td>${esc(l.source||"-")}</td>
            <td><span class="${statusClass(l.status)}">${esc(l.status||"-")}</span></td>
            <td>${esc(l.counsellor||"-")}</td><td class="${age.className}">${age.text}</td>
            <td><button class="action edit" onclick="editLead(${l.id})">Edit</button><button class="action delete" onclick="deleteLead(${l.id})">Delete</button></td>
        </tr>`;
    }).join("");
}

function renderLeads(){
    const q=(document.getElementById("search")?.value||"").toLowerCase();
    const data=leads.filter(l=>JSON.stringify(l).toLowerCase().includes(q));
    document.getElementById("leadTable").innerHTML=leadRows(data)||emptyRow(9);
}
function renderLeadPage(){
    const q=(document.getElementById("leadSearch")?.value||"").toLowerCase();
    const data=leads.filter(l=>JSON.stringify(l).toLowerCase().includes(q));
    document.getElementById("leadTable2").innerHTML=leadRows(data)||emptyRow(9);
}
function emptyRow(n){return `<tr><td colspan="${n}" style="text-align:center;padding:30px">No records found</td></tr>`;}

function openLeadModal(id=null){
    document.getElementById("leadModal").classList.remove("hidden");
    document.getElementById("leadForm").reset();
    document.getElementById("leadId").value="";
    document.getElementById("leadModalTitle").textContent=id?"Edit Lead":"Add Lead";
    if(id){
        const l=leads.find(x=>x.id===id);
        if(!l)return;
        document.getElementById("leadId").value=l.id;
        ["name","email","phone","location","course","counsellor","source","status"].forEach(k=>document.getElementById(k).value=l[k]||"");
    }
}
function closeLeadModal(){document.getElementById("leadModal").classList.add("hidden");}

document.getElementById("leadForm").addEventListener("submit",async e=>{
    e.preventDefault();
    const id=document.getElementById("leadId").value;
    const body={name:v("name"),email:v("email"),phone:v("phone"),location:v("location"),course:v("course"),counsellor:v("counsellor"),source:v("source"),status:v("status")};
    try{
        if(id)await api("/leads/"+id,{method:"PUT",body:JSON.stringify(body)});
        else await api("/leads",{method:"POST",body:JSON.stringify(body)});
        closeLeadModal();await loadAll();alert(id?"Lead updated":"Lead added");
    }catch(e){alert(e.message);}
});

async function editLead(id){openLeadModal(id);}
async function deleteLead(id){
    if(!confirm("Delete this lead?"))return;
    try{await api("/leads/"+id,{method:"DELETE"});await loadAll();}catch(e){alert(e.message);}
}

function openFollowUpModal(id=null){
    document.getElementById("followUpModal").classList.remove("hidden");
    document.getElementById("followUpForm").reset();
    document.getElementById("followId").value="";
    document.getElementById("followModalTitle").textContent=id?"Edit Follow-up":"Add Follow-up";
    if(id){
        const f=followUps.find(x=>x.id===id);
        if(!f)return;
        document.getElementById("followId").value=f.id;
        document.getElementById("followLeadId").value=f.leadId;
        document.getElementById("followDate").value=f.followUpDate;
        document.getElementById("followAction").value=f.action;
        document.getElementById("followStatus").value=f.status;
        document.getElementById("followNotes").value=f.notes||"";
    }
}
function closeFollowUpModal(){document.getElementById("followUpModal").classList.add("hidden");}

document.getElementById("followUpForm").addEventListener("submit",async e=>{
    e.preventDefault();
    const id=document.getElementById("followId").value;
    const body={leadId:Number(v("followLeadId")),followUpDate:v("followDate"),action:v("followAction"),status:v("followStatus"),notes:v("followNotes")};
    try{
        if(id)await api("/followups/"+id,{method:"PUT",body:JSON.stringify(body)});
        else await api("/followups",{method:"POST",body:JSON.stringify(body)});
        closeFollowUpModal();await loadAll();alert(id?"Follow-up updated":"Follow-up added");
    }catch(e){alert(e.message);}
});

function renderFollowUps(){
    const q=(document.getElementById("followSearch")?.value||"").toLowerCase();
    const data=followUps.filter(f=>JSON.stringify(f).toLowerCase().includes(q));
    document.getElementById("followUpTable").innerHTML=data.map(f=>`
        <tr><td>${f.id}</td><td>${f.leadId}</td><td>${f.followUpDate}</td><td>${f.action}</td><td>${esc(f.notes||"-")}</td>
        <td><span class="status">${f.status}</span></td>
        <td><button class="action edit" onclick="openFollowUpModal(${f.id})">Edit</button><button class="action delete" onclick="deleteFollowUp(${f.id})">Delete</button></td></tr>`).join("")||emptyRow(7);
}
async function deleteFollowUp(id){
    if(!confirm("Delete this follow-up?"))return;
    try{await api("/followups/"+id,{method:"DELETE"});await loadAll();}catch(e){alert(e.message);}
}

function renderReports(){
    const counts={};
    leads.forEach(l=>counts[l.status]=(counts[l.status]||0)+1);
    document.getElementById("pipeline").innerHTML=Object.entries(counts).map(([k,v])=>`<div class="report-item"><span>${k}</span><strong>${v}</strong></div>`).join("")||"<p>No data</p>";

    const sources={};
    leads.forEach(l=>sources[l.source]=(sources[l.source]||0)+1);
    const max=Math.max(1,...Object.values(sources));
    document.getElementById("sources").innerHTML=Object.entries(sources).map(([k,v])=>`<div class="report-item"><div style="width:100%"><span>${k}</span><strong style="float:right">${v}</strong><div class="bar"><i style="width:${v/max*100}%"></i></div></div></div>`).join("")||"<p>No data</p>";

    const normal=leads.filter(l=>ageInfo(l.createdAt).text==="Normal").length;
    const aging=leads.filter(l=>ageInfo(l.createdAt).text==="Ageing").length;
    const attention=leads.filter(l=>ageInfo(l.createdAt).text==="Attention").length;
    document.getElementById("ageingReport").innerHTML=`<div class="report-item"><span>Normal (0–2 days)</span><strong>${normal}</strong></div><div class="report-item"><span>Ageing (3–7 days)</span><strong>${aging}</strong></div><div class="report-item"><span>Attention (8+ days)</span><strong>${attention}</strong></div>`;
}

function v(id){return document.getElementById(id).value.trim();}
function esc(value){return String(value).replace(/[&<>"']/g,m=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"}[m]));}
