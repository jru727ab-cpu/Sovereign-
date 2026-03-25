# Sovereign- <!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sovereign Command</title>
    <style>
        body { background: #020617; color: white; font-family: sans-serif; padding: 20px; }
        .card { background: #1e293b; border: 1px solid #334155; padding: 15px; border-radius: 12px; margin-bottom: 10px; }
        .gold { color: #facc15; }
        .blue { color: #38bdf8; }
        .btn { width: 100%; padding: 15px; border-radius: 8px; border: none; font-weight: bold; margin-top: 10px; }
    </style>
</head>
<body>
    <h1>Sovereign Command</h1>
    <div class="card">
        <h3 class="blue">Admin Status: ONLINE</h3>
        <p id="genesis">Genesis Nodes: 1/1000</p>
    </div>
    
    <div class="card" style="border-left: 5px solid #10b981;">
        <h3 class="gold">The Foundry</h3>
        <p>Gold: <span id="gold-val">0.0000</span></p>
        <p>Diskon: <span id="diskon-val">0.0000</span></p>
    </div>

    <div class="card">
        <h3>Private Vault</h3>
        <code style="font-size: 0.7rem;">bc1q_SOVEREIGN_WALLET_ADDRESS</code>
        <button class="btn" style="background:#10b981; color:white;">WITHDRAW PROFITS</button>
    </div>

    <div id="navigator" class="card" style="background:#0f172a; border: 1px solid #facc15;">
        <p class="gold"><b>CEO NAVIGATOR:</b></p>
        <p style="font-size:0.8rem;">1. Upload this file to GitHub.<br>2. Run 'engine.py' in Termux.<br>3. Watch your wealth grow.</p>
    </div>
</body>
</html>