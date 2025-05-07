//const API = 'https://mentoss.onrender.com/api/admin';
const API = 'http://localhost:8081/api/admin';

//신고 리스트
async function loadReports() {
    try {
        const resNot = await fetch(`${API}/reports/not-done`, {
            credentials: 'include'
        });
        if (!resNot.ok) throw new Error(`HTTP ${resNot.status}`);
        const ct1 = resNot.headers.get('content-type') || '';
        if (!ct1.includes('application/json')) {
            throw new Error('JSON 응답이 아닙니다: ' + ct1);
        }

        const { data: notList } = await resNot.json();
        renderNotProcessed(notList);

        const resDone = await fetch(`${API}/reports/done`, {
            credentials: 'include'
        });
        if (!resDone.ok) throw new Error(`HTTP ${resDone.status}`);
        const ct2 = resNot.headers.get('content-type') || '';
        if (!ct2.includes('application/json')) {
            throw new Error('JSON 응답이 아닙니다: ' + ct2);
        }
        const { data: doneList } = await resDone.json();
        renderProcessed(doneList);

    } catch (e) {
        console.error('신고 목록 로드 실패', e);
        alert('신고 목록을 불러오는 중 오류가 발생했습니다.');
    }
}

function renderNotProcessed(list) {
    const tbody = document.getElementById('tbl-notProcessed');
    tbody.innerHTML = '';
    list.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${r.reportId}</td>
      <td>${r.reporterId}</td>
      <td>${r.targetType}</td>
      <td>${r.targetId}</td>
      <td>${r.reason}</td>
    `;
        const td = document.createElement('td');
        const btn = document.createElement('button');
        btn.textContent = '처리완료';
        btn.addEventListener('click', () => showProcessModal(r.reportId));
        td.appendChild(btn);
        tr.appendChild(td);
        tbody.appendChild(tr);
    });
}

function renderProcessed(list) {
    const tbody = document.getElementById('tbl-processed');
    tbody.innerHTML = '';
    list.forEach(r => {
        let timeText = `${r.processedAt[0]}-${r.processedAt[1]}-${r.processedAt[2]} ${r.processedAt[3]}:${r.processedAt[4]}:${r.processedAt[5]}`
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${r.reportId}</td>
      <td>${r.reporterId}</td>
      <td>${r.targetType}</td>
      <td>${r.targetId}</td>
      <td>${r.reason}</td>
      <td>${timeText}</td>
      <td>${r.processAdminId}</td>
      <td>${r.actionType}</td>
      <td>${r.actionReason}</td>
      <td>${r.suspendPeriod}</td>
    `;
        tbody.appendChild(tr);
    });
}


//상태 변경
document.getElementById('form-status')
    .addEventListener('submit', async e => {
        e.preventDefault();
        const req = {
            targetType: document.getElementById('status-type').value,
            targetId: Number(document.getElementById('status-id').value),
            status: document.getElementById('status-value').value
        };
        try {
            const res = await fetch(`${API}/status`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(req)
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            alert('상태가 변경되었습니다.');
        } catch (e) {
            console.error('상태 변경 실패', e);
            alert('상태 변경 중 오류가 발생했습니다.');
        }
    });

//삭제 복구
document.getElementById('form-delete')
    .addEventListener('submit', async e => {
        e.preventDefault();
        const action = document.getElementById('delete-action').value;
        const endpoint = action === 'soft-delete' ? 'soft-delete' : 'recover';
        const req = {
            targetType: document.getElementById('delete-type').value,
            targetId: Number(document.getElementById('delete-id').value)
        };
        try {
            const res = await fetch(`${API}/${endpoint}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(req)
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            alert(action === 'soft-delete' ? '삭제 처리되었습니다.' : '복구 처리되었습니다.');
        } catch (e) {
            console.error('Soft Delete/복구 실패', e);
            alert('처리 중 오류가 발생했습니다.');
        }
    });

//모달
const modal = document.getElementById('process-modal');
const backdrop = document.getElementById('modal-backdrop');
let currentReportId = null;

function showProcessModal(reportId) {
    currentReportId = reportId;
    document.getElementById('process-reportId').value = reportId;
    modal.style.display = 'block';
    backdrop.style.display = 'block';
}

function hideProcessModal() {
    modal.style.display = 'none';
    backdrop.style.display = 'none';
    document.getElementById('form-process').reset();
    currentReportId = null;
}
//확인 버튼
document.getElementById('process-confirm')
    .addEventListener('click', async () => {
        const dto = {
            reportId: currentReportId,
            adminId: Number(document.getElementById('process-adminId').value),
            actionType: document.getElementById('process-actionType').value,
            reason: document.getElementById('process-reason').value,
            suspendPeriod: Number(document.getElementById('process-suspendPeriod').value)
        };

        try {
            const res = await fetch(`${API}/process`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(dto)
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            hideProcessModal();
            loadReports();
        } catch (e) {
            console.error('신고 처리 실패', e);
            alert('신고 처리 중 오류가 발생했습니다.');
        }
    });
//취소 버튼
document.getElementById('process-cancel')
    .addEventListener('click', hideProcessModal);
//모달 밖에 다른데 눌러도 닫히게 하는거
backdrop.addEventListener('click', hideProcessModal);

window.addEventListener('DOMContentLoaded', loadReports);