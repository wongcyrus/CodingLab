<html>

<!-- Body Section -->
<body>

<!-- Simple Form -->
<form name="simForm" action="ex1_3.php" method="get">

    <h2>A Simple Form</h2>
    Please fill in the form and submit it on completion.
    All information will be kept strictly confidential.
    Thank you.
    <br/><br/>

    <!-- Text Fields -->
    <b>First Name:</b>
    <input type="text" name="fname"/>
    <b>Last Name:</b>
    <input type="text" name="lname"/>
    <br/><br/>

    <!-- Radio Buttons -->
    <b>Sex:</b>
    <input type="radio" name="sex[]" value="M"/>Male
    <input type="radio" name="sex[]" value="F"/>Female
    <br/><br/>

    <!-- Scrolling Menu -->
    <b>I am living in:</b>
    <select name="region">
        <option value="wc"/>
        Wan Chi
        <option value="np"/>
        North Point
        <option value="qb"/>
        Quarry Bay
        <option value="tk"/>
        Tai Koo
    </select>

    <!-- Pull-down Menu -->
    <b>I would rather live in:</b>
    <select name="wishes[]" multiple>
        <option value="mk"/>
        Mong Kok
        <option value="ymt"/>
        Yau Ma Tei
        <option value="jd"/>
        Jordan
        <option value="tst"/>
        Tsim Sha Tsui
    </select>
    <br/><br/>

    <!-- Checkboxes -->
    <b>I have visited the following places:</b>
    <br/>
    <input type="checkbox" name="visits[]" value="sh"/>Shatin
    <input type="checkbox" name="visits[]" value="u"/>University
    <input type="checkbox" name="visits[]" value="tp"/>Tai Po
    <input type="checkbox" name="visits[]" value="fl"/>Fan Ling
    <br/><br/>

    <!-- Textarea -->
    <b>Reason for my living choice :</b>
    <br/>
<textarea name="reason" cols="50" rows="2">
</textarea>
    <br/><br/>

    <!-- Submit or clear -->
    <input type="submit" name="submit"/>
    <input type="reset"/>

</form>

</body>
</html>
