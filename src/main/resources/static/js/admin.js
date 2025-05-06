const API = 'http://localhost:8081/api/admin';

// -- 1. 신고 목록 로드 & 처리 --
async function loadReports() {
    // 미처리 신고
    const notDone = await fetch(`${API}/reports/not-done`).then(r => r.json());
    populateReports('tbl-notProcessed', notDone.data, true);

    // 처리 완료 신고
    const done = await fetch(`${API}/reports/done`).then(r => r.json());
    populateReports('tbl-processed', done.data, false);
}

function populateReports(tbodyId, reports, canProcess) {
    const tbody = document.getElementById(tbodyId);
    tbody.innerHTML = '';
    reports.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${r.reportId}</td>
      <td>${r.reporterId}</td>
      <td>${r.targetType}</td>
      <td>${r.targetId}</td>
      <td>${r.reason}</td>
    `;
        const tdAction = document.createElement('td');
        if (canProcess) {
            const btn = document.createElement('button');
            btn.textContent = '처리완료';
            btn.addEventListener('click', () => processReport(r.reportId));
            tdAction.appendChild(btn);
        } else {
            tdAction.textContent = r.processedAt || '-';
        }
        tr.appendChild(tdAction);
        tbody.appendChild(tr);
    });
}

async function processReport(reportId) {
    await fetch(`${API}/reports/${reportId}/process`, {
        method: 'PUT'
    });
    loadReports();
}

// 이벤트 바인딩
document.getElementById('btn-loadReports')
    .addEventListener('click', loadReports);

// -- 2. 상태 변경 --
document.getElementById('form-status')
    .addEventListener('submit', async e => {
        e.preventDefault();
        const type = document.getElementById('status-type').value;
        const targetId = Number(document.getElementById('status-id').value);
        const status = document.getElementById('status-value').value;

        await fetch(`${API}/status`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ targetType: type, targetId, status })
        });
        alert('상태가 변경되었습니다.');
    });

// -- 3. Soft Delete & 복구 --
document.getElementById('form-delete')
    .addEventListener('submit', async e => {
        e.preventDefault();
        const type = document.getElementById('delete-type').value;
        const targetId = Number(document.getElementById('delete-id').value);
        const action = document.getElementById('delete-action').value;

        const endpoint = action === 'soft-delete' ? 'soft-delete' : 'recover';
        await fetch(`${API}/${endpoint}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ targetType: type, targetId })
        });
        alert(action === 'soft-delete' ? '삭제 처리되었습니다.' : '복구 처리되었습니다.');
    });
