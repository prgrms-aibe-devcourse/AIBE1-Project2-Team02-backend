const API = 'http://localhost:8081/api/admin';

async function loadReports() {
    try {
        const resNot = await fetch(`${API}/reports/not-done`);
        if (!resNot.ok) throw new Error(`HTTP ${resNot.status}`);
        const { data: notList } = await resNot.json();
        renderNotProcessed(notList);

        const resDone = await fetch(`${API}/reports/done`);
        if (!resDone.ok) throw new Error(`HTTP ${resDone.status}`);
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
        btn.addEventListener('click', () => processReport(r.reportId));
        td.appendChild(btn);
        tr.appendChild(td);
        tbody.appendChild(tr);
    });
}

function renderProcessed(list) {
    const tbody = document.getElementById('tbl-processed');
    tbody.innerHTML = '';
    list.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${r.reportId}</td>
      <td>${r.reporterId}</td>
      <td>${r.targetType}</td>
      <td>${r.targetId}</td>
      <td>${r.reason}</td>
      <td>${r.reasonType}</td>
      <td>${r.processedAt ? new Date(r.processedAt).toLocaleString() : '-'}</td>
      <td>${r.processAdminId}</td>
    `;
        tbody.appendChild(tr);
    });
}

async function processReport(reportId) {
    try {
        await fetch(`${API}/reports/${reportId}/process`, {
            method: 'PUT'
        });
        loadReports();
    } catch (e) {
        console.error('신고 처리 실패', e);
        alert('신고 처리 중 오류가 발생했습니다.');
    }
}

// 2. 상태 변경
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

// 3. Soft Delete & 복구
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

// 초기 로드
window.addEventListener('DOMContentLoaded', loadReports);